package com.barbulescu.codedojo.exercise0003

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class Exercise0003Test {

    private val workflow: OrderWorkflow = ExerciseJava0003()

    @Test
    fun `create order starts in confirmed state`() {
        val result = workflow.create(createOrder())

        val order = acceptedOrder(result)
        assertThat(order.orderId()).isEqualTo("order-1")
        assertThat(order.customerId()).isEqualTo("customer-1")
        assertThat(order.lines()).hasSize(1)
        assertThat(order.state()).isInstanceOf(Confirmed::class.java)
    }

    @Test
    fun `add item preserves insertion order and recalculates total`() {
        val order = acceptedOrder(workflow.create(createOrder()))

        val result = workflow.handle(
            order,
            AddItem("sku-2", "Mouse", 1, money("10.00"))
        )

        val updatedOrder = acceptedOrder(result)
        assertThat(updatedOrder.lines().map { it.product().id() })
            .containsExactly("sku-1", "sku-2")
        assertThat(updatedOrder.total().amount())
            .isEqualByComparingTo(BigDecimal("49.98"))
    }

    @Test
    fun `apply discount reduces order total`() {
        val order = acceptedOrder(workflow.create(createOrder()))

        val result = workflow.handle(order, ApplyDiscount("SAVE10", BigDecimal("10")))

        val updatedOrder = acceptedOrder(result)
        assertThat(updatedOrder.discount()!!.code()).isEqualTo("SAVE10")
        assertThat(updatedOrder.total().amount())
            .isEqualByComparingTo(BigDecimal("35.982"))
    }

    @Test
    fun `ship order moves confirmed order to shipped state`() {
        val order = acceptedOrder(workflow.create(createOrder()))

        val result = workflow.handle(order, ShipOrder("TRACK-123"))

        val updatedOrder = acceptedOrder(result)
        assertThat(updatedOrder.state()).isInstanceOf(Shipped::class.java)
        assertThat((updatedOrder.state() as Shipped).trackingNumber()).isEqualTo("TRACK-123")
    }

    @Test
    fun `cancel order moves confirmed order to cancelled state`() {
        val order = acceptedOrder(workflow.create(createOrder()))

        val result = workflow.handle(order, CancelOrder("customer changed mind"))

        val updatedOrder = acceptedOrder(result)
        assertThat(updatedOrder.state()).isInstanceOf(Cancelled::class.java)
        assertThat((updatedOrder.state() as Cancelled).reason()).isEqualTo("customer changed mind")
    }

    @Test
    fun `cannot cancel shipped order`() {
        val shippedOrder = acceptedOrder(
            workflow.handle(
                acceptedOrder(workflow.create(createOrder())),
                ShipOrder("TRACK-123")
            )
        )

        val result = workflow.handle(shippedOrder, CancelOrder("too late"))

        val rejected = rejectedResult(result)
        assertThat(rejected.reason()).isEqualTo("Only confirmed orders can be cancelled")
    }

    @Test
    fun `return order moves shipped order to returned state`() {
        val shippedOrder = acceptedOrder(
            workflow.handle(
                acceptedOrder(workflow.create(createOrder())),
                ShipOrder("TRACK-123")
            )
        )

        val result = workflow.handle(shippedOrder, ReturnOrder("wrong size"))

        val updatedOrder = acceptedOrder(result)
        assertThat(updatedOrder.state()).isInstanceOf(Returned::class.java)
        assertThat((updatedOrder.state() as Returned).reason()).isEqualTo("wrong size")
    }

    @Test
    fun `invalid command data is rejected at construction time`() {
        assertThatThrownBy { ShipOrder(" ") }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("trackingNumber")

        assertThatThrownBy { AddItem("sku-2", "Mouse", 0, money("10.00")) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("quantity")

        assertThatThrownBy { ApplyDiscount("SAVE", BigDecimal("101")) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessageContaining("percent")
    }

    @Test
    fun `value objects commands results and data-carrying states are records`() {
        val recordTypes = listOf(
            Order::class.java,
            Product::class.java,
            Money::class.java,
            OrderLine::class.java,
            AppliedDiscount::class.java,
            CreateOrder::class.java,
            AddItem::class.java,
            ApplyDiscount::class.java,
            ShipOrder::class.java,
            CancelOrder::class.java,
            ReturnOrder::class.java,
            Accepted::class.java,
            Rejected::class.java,
            Shipped::class.java,
            Cancelled::class.java,
            Returned::class.java,
        )

        assertThat(recordTypes).allSatisfy { type ->
            assertThat(type.isRecord)
                .describedAs("${type.simpleName} should be migrated to a record")
                .isTrue()
        }
    }

    @Test
    fun `core domain hierarchies are sealed`() {
        val sealedTypes = listOf(
            OrderCommand::class.java,
            OrderResult::class.java,
            FulfillmentState::class.java,
        )

        assertThat(sealedTypes).allSatisfy { type ->
            assertThat(type.isSealed)
                .describedAs("${type.simpleName} should be migrated to a sealed type")
                .isTrue()
        }
    }

    @Test
    fun `sealed hierarchies permit the expected variants`() {
        assertThat(permittedNames(OrderCommand::class.java))
            .containsExactlyInAnyOrder(
                "CreateOrder",
                "AddItem",
                "ApplyDiscount",
                "ShipOrder",
                "CancelOrder",
                "ReturnOrder",
            )
        assertThat(permittedNames(OrderResult::class.java))
            .containsExactlyInAnyOrder("Accepted", "Rejected")
        assertThat(permittedNames(FulfillmentState::class.java))
            .containsExactlyInAnyOrder("Confirmed", "Shipped", "Cancelled", "Returned")
    }

    private fun createOrder(): CreateOrder =
        CreateOrder(
            "order-1",
            "customer-1",
            listOf(OrderLine(Product("sku-1", "Keyboard"), 2, money("19.99")))
        )

    private fun money(amount: String): Money =
        Money(BigDecimal(amount), "CHF")

    private fun acceptedOrder(result: OrderResult): Order {
        assertThat(result).isInstanceOf(Accepted::class.java)
        return (result as Accepted).order()
    }

    private fun rejectedResult(result: OrderResult): Rejected {
        assertThat(result).isInstanceOf(Rejected::class.java)
        return result as Rejected
    }

    private fun permittedNames(type: Class<*>): List<String> =
        type.permittedSubclasses.orEmpty().map { it.simpleName }
}
