package whoana.finance.data;

public class Position {

	String portfolioId;
	
	String symbol;
	
	String tradeDay;
	
	double price;
	
	int qty;
	
	double amt;
 

	public String getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
 

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public void setTradeDay(String tradeDay) {
		this.tradeDay = tradeDay;		
	}
	
	public String getTradeDay() {
		return tradeDay;
	}
	
}
