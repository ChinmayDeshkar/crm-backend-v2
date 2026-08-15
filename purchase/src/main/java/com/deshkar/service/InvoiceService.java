package com.deshkar.service;

import com.deshkar.model.Invoice;

public interface InvoiceService {

    Invoice generateInvoice(Long purchaseId) throws Exception;

    byte[] downloadInvoice(Long purchaseId) throws Exception;
}
