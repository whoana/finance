package whoana.finance.data;

public class Performance {

	public int CURRENCY_USD = 0;

	public int CURRENCY_KRW = 1;

	int currency = CURRENCY_USD;

	double returnRate;

	double returnAmt;

	double currencyRate;

	String fromDate;

	String toDate;

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	public double getReturnRate() {
		return returnRate;
	}

	public void setReturnRate(double returnRate) {
		this.returnRate = returnRate;
	}

	public double getReturnAmt() {
		return returnAmt;
	}

	public void setReturnAmt(double returnAmt) {
		this.returnAmt = returnAmt;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public double getCurrencyRate() {
		return currencyRate;
	}

	public void setCurrencyRate(double currencyRate) {
		this.currencyRate = currencyRate;
	}




}
