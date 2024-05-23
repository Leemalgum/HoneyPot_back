package com.beeSpring.beespring.service.bid;

import com.beeSpring.beespring.domain.bid.Bid;
import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.bid.BidLogRepository;
import com.beeSpring.beespring.repository.bid.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

@Service
public class BidLogServiceImpl implements BidLogService {


    private final BidLogRepository bidLogRepository;
    private final ExecutorService executorService;
    private final ProductRepository productRepository;


    public BidLogServiceImpl(BidLogRepository bidLogRepository, ProductRepository productRepository) {
        this.bidLogRepository = bidLogRepository;
        this.productRepository = productRepository;
        this.executorService = Executors.newVirtualThreadPerTaskExecutor();
    }




    @Override
    @Transactional
    public Bid placeBid(Product product, User user, int price) {
        return CompletableFuture.supplyAsync(() -> {
            Integer maxPrice = bidLogRepository.findMaxPriceByProduct(product);
            if (maxPrice != null && price <= maxPrice) {
                throw new IllegalArgumentException("입찰 가격은 현재 입찰 최고가보다 높아야 합니다.");
            }
            // Product의 가격 업데이트
            productRepository.updateProductPrice(product.getProductId(), price);

            Bid bid = new Bid(product, user, price); // 입찰 객체 생성
            Bid savedBid = bidLogRepository.save(bid); // 입찰 객체를 데이터베이스에 저장

            return savedBid; // 입찰 객체를 데이터베이스에 저장
        }, executorService).join(); // 가상 스레드를 사용하여 비동기 작업을 실행하고, 결과를 동기적으로 기다림
    }

    @Override
    @Transactional(readOnly = true)
    public List<Bid> getBidsForProduct(Product product) {
        return CompletableFuture.supplyAsync(() -> bidLogRepository.findByProduct(product), executorService).join();
    }
}
