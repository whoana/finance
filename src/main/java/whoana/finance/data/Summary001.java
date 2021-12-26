package whoana.finance.data;
public class Summary001 {

    String portfolioId;
    String symbol;
    String tday;
    int qty;
    double price;
    double amt;
    double curPrice;
    double curAmt;
    double earning;
    double earningRate;
    double dailyEarning;
    double dailyEarningRate;
    String regDate;

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

    public String getTday() {
        return tday;
    }

    public void setTday(String tday) {
        this.tday = tday;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getAmt() {
        return amt;
    }

    public void setAmt(double amt) {
        this.amt = amt;
    }

    public double getCurPrice() {
        return curPrice;
    }

    public void setCurPrice(double curPrice) {
        this.curPrice = curPrice;
    }

    public double getCurAmt() {
        return curAmt;
    }

    public void setCurAmt(double curAmt) {
        this.curAmt = curAmt;
    }

    public double getEarning() {
        return earning;
    }

    public void setEarning(double earning) {
        this.earning = earning;
    }

    public double getEarningRate() {
        return earningRate;
    }

    public void setEarningRate(double earningRate) {
        this.earningRate = earningRate;
    }

    public double getDailyEarning() {
        return dailyEarning;
    }

    public void setDailyEarning(double dailyEarning) {
        this.dailyEarning = dailyEarning;
    }

    public double getDailyEarningRate() {
        return dailyEarningRate;
    }

    public void setDailyEarningRate(double dailyEarningRate) {
        this.dailyEarningRate = dailyEarningRate;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
}
