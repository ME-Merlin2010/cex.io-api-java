package zackurben.cex.api;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
// only is(Class) is deprecated
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.anyOf;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CexAPITest {

	private CexAPI cexApi;
	private String username = "up114757661";

	@BeforeEach
	void setUp() throws Exception {

		String apiKey = "<api-key>";
		String apiSecret = "<api-secret>";

		cexApi = new CexAPI(username, apiKey, apiSecret);

	}

	@Test
	void testCexAPI() {
		// Fetch the CexAPI account data.
		assertThat("Testing User Data", cexApi.toString(), containsString(username));
	}

	@Test
	void testTicker() {
		// Fetch the ticker data, for the currency pair.
		assertThat("Testing method: ticker(\\\"ZEC/EUR\\\")", cexApi.ticker("ZEC/EUR") + "\n", containsString("last"));
	}

	@Test
	void testLastPrice() {
		// Fetch the last order price for the currency pair.
		assertThat("Testing method: lastPrice(\\\"ZEC\\\", \\\"EUR\\\")", cexApi.lastPrice("ZEC", "EUR"),
				containsString("lprice"));
	}

	@Test
	void testConvert() {
		// Fetch the price conversion from the Major to Minor currency.
		assertThat("Testing method: convert(\\\"ZEC\\\", \\\"EUR\\\", 2)", cexApi.convert("ZEC", "EUR", 2f),
				containsString("amnt"));
	}

	@Test
	void testChart() {
		// Fetch the historical data points for the Major to Minor currency.
		assertThat("Testing method: chart(\\\"ZEC\\\", \\\"EUR\\\", 24, 100)", cexApi.chart("ZEC", "EUR", 24, 2),
				containsString("price"));
	}

	@Test
	void testOrderBook() {
		// Fetch the order book data, for the currency pair.
		assertThat("Testing method: orderBook(\\\"ZEC/EUR\\\")", cexApi.orderBook("ZEC/EUR"), containsString("bids"));
	}

	@Test
	void testTradeHistory() {
		// Fetch the trade history data, for the currency pair.
		assertThat("Testing method: tradeHistory(\\\"XLM/EUR\\\")", cexApi.tradeHistory("XLM/EUR", 1),
				containsString("price"));
	}

	@Test
	void testBalanceAndPlaceOrderAndOpenOrdersAndCancelOrder() {
		// all private transactions in one test because of the order of the nonce
		
		// Fetch the account balance data.
		// the String "username" and the username itself
		assertThat("Testing method: balance()", cexApi.balance(), anyOf(containsString("username"), containsString(username)));

		// Place an order, for the currency pair, with the given amount and price.
		String orderResult = cexApi.placeOrder("XLM/EUR", "sell", 80f, 2.00000000f);
		System.out.println(orderResult + "\n");
		assertThat("Testing method: placeOrder(\\\"XLM/EUR\\\")", orderResult, containsString("\"price\":\"2\""));
		
		// Fetch the account open orders, for the currency pair.
		assertThat("Testing method: openOrders(\\\"XLM/EUR\\\")", cexApi.openOrders("XLM/EUR"),
				anyOf(containsString("\"type\":\"sell\""), containsString("\"price\":\"2\"")));

		// Cancel the account order with the given ID.
		String orderResultId = orderResult.split(",")[1].split(":")[1].split("\"")[1];
		assertThat("Testing method: cancelOrder(" + orderResult + " with ID: " + orderResultId + ")",
				cexApi.cancelOrder(orderResultId), is("true"));
		assertThat("Testing method: openOrders(\\\"XLM/EUR\\\") - order canceled", cexApi.openOrders("XLM/EUR"),
				not(containsString("\"price\":\"40000\"")));
	}

}
