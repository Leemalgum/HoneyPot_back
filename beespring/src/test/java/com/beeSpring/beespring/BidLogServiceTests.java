package com.beeSpring.beespring;

import com.beeSpring.beespring.domain.bid.Bid;
import com.beeSpring.beespring.domain.bid.Product;
import com.beeSpring.beespring.domain.user.User;
import com.beeSpring.beespring.repository.bid.BidLogRepository;
import com.beeSpring.beespring.service.bid.BidLogServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import java.util.stream.Collectors;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class BidLogServiceTests {
//    private static final Logger logger = LoggerFactory.getLogger(BidLogServiceTests.class);
//
//    @Mock
//    private BidLogRepository bidLogRepository;
//
//    @InjectMocks
//    private BidLogServiceImpl bidLogService;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        bidLogService = new BidLogServiceImpl(bidLogRepository); // Constructor injection
//    }
//
//    @Test
//    void testConcurrentPlaceBid() throws InterruptedException, ExecutionException {
//        logger.info("Starting testConcurrentPlaceBid");
//
//        // given
//        Product product = new Product();
//        User user = new User();
//        int initialPrice = 100;
//
//        when(bidLogRepository.save(any(Bid.class))).thenAnswer(invocation -> {
//            Bid bid = invocation.getArgument(0);
//            return bid;
//        });
//
//        when(bidLogRepository.findMaxPriceByProduct(any(Product.class))).thenReturn(initialPrice);
//
//        // Create 1000 concurrent bid tasks
//        List<CompletableFuture<Bid>> futures = IntStream.range(0, 1000)
//                .mapToObj(i -> CompletableFuture.supplyAsync(() -> {
//                    int bidPrice = initialPrice + i + 1; // Ensure each bid is higher than the previous one
//                    logger.debug("Placing bid number {} with price {}", i, bidPrice);
//                    return bidLogService.placeBid(product, user, bidPrice);
//                }, Executors.newVirtualThreadPerTaskExecutor())) // 가상 스레드를 사용하여 비동기 작업 실행
//                .collect(Collectors.toList());
//
//        // Wait for all tasks to complete
//        List<Bid> bids = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
//                .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()))
//                .get();
//
//        // then
//        assertEquals(1000, bids.size());
//        verify(bidLogRepository, times(1000)).save(any(Bid.class));
//
//        for (int i = 0; i < 1000; i++) {
//            assertEquals(initialPrice + i + 1, bids.get(i).getPrice());
//        }
//
//        logger.info("Completed testConcurrentPlaceBid");
//    }
//
//    @Test
//    void testPlaceBidWithLowerPrice() {
//        logger.info("Starting testPlaceBidWithLowerPrice");
//
//        // given
//        Product product = new Product();
//        User user = new User();
//        int currentHighestPrice = 100;
//        int lowerPrice = 90;
//
//        when(bidLogRepository.findMaxPriceByProduct(product)).thenReturn(currentHighestPrice);
//
//        // when
//        CompletionException exception = assertThrows(CompletionException.class, () -> {
//            bidLogService.placeBid(product, user, lowerPrice);
//        });
//
//        // then
//        assertNotNull(exception.getCause());
//        assertTrue(exception.getCause() instanceof IllegalArgumentException);
//        assertEquals("입찰 가격은 현재 입찰 최고가보다 높아야 합니다.", exception.getCause().getMessage());
//
//        verify(bidLogRepository, never()).save(any(Bid.class));
//
//        logger.info("Completed testPlaceBidWithLowerPrice");
//    }
//
//    @Test
//    void testGetBidsForProduct() {
//        logger.info("Starting testGetBidsForProduct");
//
//        // given
//        Product product = new Product();
//        Bid bid1 = new Bid(product, new User(), 100);
//        Bid bid2 = new Bid(product, new User(), 200);
//
//        when(bidLogRepository.findByProduct(any(Product.class))).thenReturn(List.of(bid1, bid2));
//
//        // when
//        logger.debug("Calling getBidsForProduct with product: {}", product);
//
//        List<Bid> bids = CompletableFuture.supplyAsync(() -> {
//            logger.debug("Inside CompletableFuture for getBidsForProduct");
//            return bidLogService.getBidsForProduct(product);
//        }).join();
//
//        // then
//        assertNotNull(bids);
//        assertEquals(2, bids.size());
//        assertEquals(bid1.getPrice(), bids.get(0).getPrice());
//        assertEquals(bid2.getPrice(), bids.get(1).getPrice());
//
//        logger.debug("Retrieved bids: {}", bids);
//
//        verify(bidLogRepository, times(1)).findByProduct(any(Product.class));
//
//        logger.info("Completed testGetBidsForProduct");
//    }
}
