package com.gabia.bshop.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString(exclude = {"item", "order"})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
	name = "order_item",
	indexes = {})
@Entity
public class OrderItem extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "item_id", nullable = false)
	private Item item;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "option_id", nullable = false)
	private ItemOption option;

	@Column(nullable = false)
	private int orderCount;

	@Column(nullable = false)
	private long price;

	@Builder
	private OrderItem(
		final Long id,
		final Item item,
		final Order order,
		final ItemOption option,
		final int orderCount,
		final long price) {
		this.id = id;
		this.item = item;
		this.order = order;
		this.option = option;
		this.orderCount = orderCount;
		this.price = price;
	}

	public static OrderItem createOrderItem(final ItemOption itemOption,
		final Order order, final int count) {
		OrderItem orderItem = OrderItem.builder()
			.item(itemOption.getItem())
			.order(order)
			.option(itemOption)
			.orderCount(count)
			.price((itemOption.getItem().getBasePrice() + itemOption.getOptionPrice()))
			.build();

		itemOption.decreaseStockQuantity(count);
		order.calculateTotalPrice(orderItem, count);

		return orderItem;
	}

	public void cancel() {
		option.increaseStockQuantity(this.orderCount);
	}

	@Override
	public boolean equals(final Object that) {
		if (this == that) {
			return true;
		}
		if (that == null || getClass() != that.getClass()) {
			return false;
		}
		final OrderItem orderItem = (OrderItem)that;
		return getId().equals(orderItem.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getId());
	}
}
