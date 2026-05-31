package org.Lumi.service;

import org.Lumi.model.Purchase;
import org.Lumi.repo.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public List<Purchase> getAllPurchases() {
        return purchaseRepository.findAll();
    }

    public Optional<Purchase> getPurchaseById(Integer id) {
        return purchaseRepository.findById(id);
    }

    public Purchase createPurchase(Purchase purchase) {
        return purchaseRepository.save(purchase);
    }

    public Purchase updatePurchase(Integer id, Purchase purchaseDetails) {
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));

        if (purchaseDetails.getPlayer() != null) {
            purchase.setPlayer(purchaseDetails.getPlayer());
        }
        if (purchaseDetails.getSkin() != null) {
            purchase.setSkin(purchaseDetails.getSkin());
        }
        purchase.setTransactionDate(purchaseDetails.getTransactionDate());

        return purchaseRepository.save(purchase);
    }

    public void deletePurchase(Integer id) {
        purchaseRepository.deleteById(id);
    }
}
