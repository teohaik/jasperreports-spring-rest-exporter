package gr.teohaik.jasperdemo.service;

import gr.teohaik.jasperdemo.beans.AddressModel;
import gr.teohaik.jasperdemo.beans.OrderEntryModel;
import gr.teohaik.jasperdemo.beans.OrderModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MockOrderService {

    public OrderModel getOrderByCode(final String code) {

        return order(code);

    }

    private OrderModel order(String code) {
        return new OrderModel(code, address(), entries());
    }

    private AddressModel address() {
        return new AddressModel("Theodore",
                "Chaikalis",
                "Egnatia 1",
                "54321",
                "Thessaloniki",
                "Greece");
    }

    private List<OrderEntryModel> entries() {
        return new ArrayList<>() {
            {
                add(new OrderEntryModel("Charge Fees for POS 123 12/3/21", 12, 1500d));
                add(new OrderEntryModel("Charge Fees for POS 123 13/3/21", 24, 2400d));
                add(new OrderEntryModel("Charge Fees for POS 123 14/3/21", 127, 1400d));
                add(new OrderEntryModel("Charge Fees for POS 123 15/3/21", 32, 200d));
            }
        };
    }
}
