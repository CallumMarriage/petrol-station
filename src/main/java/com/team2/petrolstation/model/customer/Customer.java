package com.team2.petrolstation.model.customer;

/**
 * The customer interface ensures that the all customers can both manage transactions, they can also be treated the same when be assigned to their queues.
 *
 * @author callummarriage
 */
public interface Customer {

    Double getSize();

    Boolean act(Integer value);
}
