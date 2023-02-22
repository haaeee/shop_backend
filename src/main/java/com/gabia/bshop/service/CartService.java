package com.gabia.bshop.service;

import static com.gabia.bshop.exception.ErrorCode.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gabia.bshop.dto.CartDto;
import com.gabia.bshop.dto.response.CartResponse;
import com.gabia.bshop.entity.ItemOption;
import com.gabia.bshop.exception.NotFoundException;
import com.gabia.bshop.mapper.CartResponseMapper;
import com.gabia.bshop.repository.CartRepository;
import com.gabia.bshop.repository.ItemOptionRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CartService {

	private final CartRepository cartRepository;
	private final ItemOptionRepository itemOptionRepository;

	@Transactional
	public CartDto save(final Long memberId, final CartDto cartDto) {
		checkItemAndItemOption(cartDto);
		return cartRepository.save(memberId, cartDto);
	}

	private void checkItemAndItemOption(final CartDto cartDto) {
		if (!itemOptionRepository.existsByItem_IdAndIdAndStockQuantityIsGreaterThanEqual(cartDto.itemId(),
			cartDto.itemOptionId(), cartDto.orderCount())) {
			throw new NotFoundException(ITEM_OPTION_NOT_FOUND_EXCEPTION, cartDto.itemId(), cartDto.itemOptionId());
		}
	}

	public List<CartResponse> findAll(final Long memberId) {
		final List<CartDto> cartDtoList = cartRepository.findAllByMemberId(memberId);
		final List<ItemOption> itemOptionList = itemOptionRepository.findWithItemAndCategoryAndImageByItemIdListAndIdList(
			cartDtoList);

		return CartResponseMapper.INSTANCE.from(itemOptionList, cartDtoList);
	}

	@Transactional
	public void delete(final Long memberId, final CartDto cartDto) {
		cartRepository.delete(memberId, cartDto);
	}
}