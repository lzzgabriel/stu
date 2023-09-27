package com.devs.gama.stu.enums;

import java.util.Arrays;

import com.devs.gama.stu.exceptions.CallNotFoundException;

public enum PadraoPaginacaoViews {

	VIEW_RESULT_10(10);

	private final int value;

	PadraoPaginacaoViews(int value) {
		this.value = value;
	}

	public int getValue() {
		
		return this.value;
	}
	

	public static PadraoPaginacaoViews parse(int value) {
		return Arrays.asList(PadraoPaginacaoViews.values()).stream().filter(s -> s.getValue() == value).findFirst()
				.orElseThrow(() -> new CallNotFoundException("Pattern of pagination view/table not found: " + value));
	}

}
