package com.globits.office;

public class OfficeConst {
	public static enum ReceiptExpenditureEnum{
		Receipt(1),
		Expenditure(-1);
		private int value;  
		private ReceiptExpenditureEnum(int value) {
		    this.value = value;
		}
		public int getValue() {
			return value;
		}
	}
}
