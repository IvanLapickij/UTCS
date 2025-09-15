package payment;

import java.util.*;

import com.paypal.api.payments.*;
import com.paypal.base.rest.*;

import Members.Member;
import Members.MemberData;
import Reservation_Page.Booking;
import loginController.Helper;

public class PaymentServices {

    private static final String CLIENT_ID = "AVvvMcq9Zz1e1wXTYpPJ-gQ9mDLURNudrmeOJvS1rshkdtKvWNMOuRpTtR7KvVEURBrnQbyhgu3QH_5s";
    private static final String CLIENT_SECRET = "EIrQ97d6LFB7V1BGwNpoUF-Bms61wK-n4L5chQsgWsarz_LNjjZ0Y8zLSnN3s6b7_jff9yjY6rFIz4Ci";
    private static final String MODE = "sandbox";
    private final String localPort;

    public PaymentServices() {
        this.localPort = "8080";
    }

    public String authorizePayment(Booking orderDetail) throws PayPalRESTException {
        Member member = Helper.getBean("memberData", MemberData.class).getMemberUsername(orderDetail.getMemberUsername());
    	
    	Payer payer = getPayerInformation(member);
        RedirectUrls redirectUrls = getRedirectURLs();
        List<Transaction> listTransaction = getTransactionInformation(orderDetail);

        Payment requestPayment = new Payment();
        requestPayment.setTransactions(listTransaction);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");

        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        Payment approvedPayment = requestPayment.create(apiContext);
        // Returning the approval URL after payment authorization
        return getApprovalLink(approvedPayment);
    }

    private Payer getPayerInformation(Member m) {
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(m.getFirstName()).setLastName(m.getLastName()).setEmail(m.getEmailAddress());
        // .setEmail("sb-7rng945296517@personal.example.com");)
        payer.setPayerInfo(payerInfo);

        System.out.println("Payer Info: " + payerInfo.toString());
        return payer;
    }

    private RedirectUrls getRedirectURLs() {
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:" + localPort + "/utcs/cancel.xhtml");
        redirectUrls.setReturnUrl("http://localhost:" + localPort + "/utcs/confirm.xhtml");
        return redirectUrls;
    }

    private List<Transaction> getTransactionInformation(Booking orderDetail) {
        Details details = new Details();
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(Double.toString(orderDetail.getTotalPrice()));
        amount.setDetails(details);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription(orderDetail.getWorkspace().getName());

        ItemList itemList = new ItemList();
        List<Item> items = new ArrayList<>();
        Item item = new Item();
        item.setCurrency("EUR");
        item.setName(orderDetail.getWorkspace().getName());
        item.setQuantity("1");
        item.setPrice(String.format("%.2f", orderDetail.getTotalPrice()));
        items.add(item);
        itemList.setItems(items);
        transaction.setItemList(itemList);

        List<Transaction> listTransaction = new ArrayList<>();
        listTransaction.add(transaction);
        return listTransaction;
    }

    private String getApprovalLink(Payment approvedPayment) {
        List<Links> links = approvedPayment.getLinks();
        String approvalLink = null;
        for (Links link : links) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                approvalLink = link.getHref();
                break;
            }
        }
        return approvalLink;
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        Payment payment = new Payment().setId(paymentId);
        APIContext apiContext = new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);

        return payment.execute(apiContext, paymentExecution);
    }
}

