CREATE TABLE `User` (
                         `serial_number`	VARCHAR(300)	NOT NULL,
                         `user_id`	VARCHAR(50)	NULL,
                         `role_id`	ENUM('ADMIN', 'USER')	NOT NULL,
                         `password`	VARCHAR(50)	NOT NULL,
                         `first_name`	VARCHAR(50)	NULL,
                         `last_name`	VARCHAR(50)	NULL,
                         `nickname`	VARCHAR(50)	NOT NULL,
                         `email`	VARCHAR(100)	NOT NULL,
                         `mobile_number`	VARCHAR(50)	NULL,
                         `registration_date`	DATETIME	NULL,
                         `birthdate`	INT	NULL,
                         `reported_cnt`	INT	NOT NULL DEFAULT 0,
                         `state`	ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED', 'PENDING', 'DEACTIVATED')	NOT NULL	DEFAULT 'ACTIVE',
                         `reason`	VARCHAR(100)	NULL,
                         `suspended`	TINYINT	NOT NULL	DEFAULT 0	COMMENT '0이 정지된 적 없음. 1이 정지 기록 있음.',
                         `mod_date`	DATETIME	NOT NULL,
                         `refresh_token`	varchar(300)	NULL,
                         `profile_image`	BLOB	NULL,
                         `tag1`	VARCHAR(50)	NOT NULL,
                         `tag2`	VARCHAR(50)	NULL,
                         `tag3`	VARCHAR(50)	NULL
 );


CREATE TABLE `Role` (
                        `role_id`	TINYINT	NOT NULL	COMMENT 'AUTO_INCREMENT',
                        `level`	ENUM('ADMIN', 'USER')	NOT NULL
);

CREATE TABLE `Shipping` (
                            `shipping_id`	VARCHAR(50)	NOT NULL,
                            `id2`	BIGINT	NOT NULL,
                            `deliver_status`	ENUM('PENDING', 'SHIPPING', 'DELIVERD', 'RETURNING', 'CANCELLED')	NULL,
                            `Field`	DATE	NULL
);

CREATE TABLE `Subscription` (
                                `sub_id`	int	NOT NULL,
                                `serial_number`	VARCHAR(100)	NOT NULL,
                                `fee_discount`	int	NULL,
                                `boost`	int	NULL,
                                `space_rental`	varchar(255)	NULL
);

CREATE TABLE `Board` (
                         `board_id`	BIGINT	NOT NULL,
                         `serial_number`	VARCHAR(100)	NOT NULL,
                         `category`	VARCHAR(100)	NOT NULL,
                         `title`	VARCHAR(100)	NOT NULL,
                         `content`	VARCHAR(1000)	NOT NULL,
                         `enrolled_time`	DATETIME	NOT NULL
);

CREATE TRIGGER set_price_unit_before_insert
    BEFORE INSERT ON Product
    FOR EACH ROW
    SET NEW.price_unit = round(NEW.start_price / 10);

CREATE TABLE `Product` (
                           `product_id`	VARCHAR(50)	NOT NULL,
                           `idol_id`	int	NOT NULL,
                           `ptype_id`	int	NOT NULL,
                           `serial_number`	VARCHAR(100)	NOT NULL,
                           `product_name`	VARCHAR(50)	NOT NULL,
                           `image1`	VARCHAR(100)	NOT NULL,
                           `image2`	VARCHAR(100)	NOT NULL,
                           `image3`	VARCHAR(100)	NULL,
                           `image4`	VARCHAR(100)	NULL,
                           `image5`	VARCHAR(100)	NULL,
                           `product_info`	VARCHAR(1000)	NOT NULL,
                           `price`	int	NOT NULL	DEFAULT 1000,
                           `price_unit`	int	NULL,
                           `buy_now`	int	NULL	DEFAULT 0	COMMENT '0설정 시 즉시구매 x',
                           `time_limit`	DATETIME	NULL	COMMENT '경매 기간',
                           `view`	BIGINT	NULL	DEFAULT 0,
                           `start_price`	int	NOT NULL,
                           `registration_date`	DATETIME	NOT NULL,
                           `bid_cnt`	Int	NOT NULL	DEFAULT 0,
                           `request_id`	VARCHAR(50)	NOT NULL,
                           `request_time`	DATETIME	NOT NULL,
                           `storage_status`	ENUM('PENDING','PROCESSING','READY','SELLING')	NOT NULL	COMMENT 'PENDING = 입고 대기중, PROCESSING=검수 진행중, READY=검수 및 입고 완료,SELLING=판매중'
);

CREATE TABLE `Report` (
                          `report_id`	BIGINT	NOT NULL,
                          `serial_number`	VARCHAR(100)	NOT NULL,
                          `post_id`	VARCHAR(255)	NOT NULL,
                          `type`	ENUM('SCAM','SPAM','SEXUAL','PRIVACY','AD','SWEAR','ETC')	NOT NULL	COMMENT '사기, 도배, 선정성, 개인정보유출,광고,욕설,기타',
                          `content`	VARCHAR(500)	NULL,
                          `enrolled_time`	DATETIME	NOT NULL,
                          `status`	ENUM('PENDING', 'ACCEPTED', 'REJECTED')	NOT NULL	DEFAULT 'PENDING',
                          `mod_date`	DATETIME	NULL,
                          `result`	VARCHAR(500)	NULL,
                          `reporter_id`	VARCHAR(50)	NOT NULL	COMMENT '신고자'
);

CREATE TABLE `Reply` (
                         `reply_id`	BIGINT	NOT NULL,
                         `board_id`	BIGINT	NOT NULL,
                         `reply`	VARCHAR(500)	NOT NULL,
                         `enrolled_time`	DATETIME	NOT NULL
);

CREATE TABLE `Finance` (
                           `finance_id`	BIGINT	NOT NULL	COMMENT 'AUTO_INCREMENT',
                           `date`	DATETIME	NOT NULL,
                           `revenue_id`	BIGINT	NOT NULL,
                           `expense_id`	BIGINT	NOT NULL,
                           `net_income`	DECIMAL	NULL,
                           `cateogry`	ENUM('REVENUE','EXPENSES','PROFIT')	NULL,
                           `description`	VARCHAR(1000)	NULL,
                           `created_time`	DATETIME	NOT NULL,
                           `updated_time`	DATETIME	NOT NULL
);

CREATE TABLE `Expense` (
                           `expense_id`	BIGINT	NOT NULL,
                           `category`	ENUM('SERVER','AD','SERVICE','ETC')	NOT NULL,
                           reason VARCHAR(200),
                           `amount`	DECIMAL	NOT NULL,
                           `status`	ENUM('PENDING','DONE')	NOT NULL DEFAULT 'PENDING',
                           `approved_by`	VARCHAR(50)	NOT NULL	COMMENT '세션 값에서 읽어온다'
);

CREATE TABLE `Revenue` (
                           `revenue_id`	BIGINT	NOT NULL,
                           `status`	ENUM('PENDING','DONE')	NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE `Payment` (
                           `order_id`	VARCHAR(50)	NOT NULL,
                           `Field`	VARCHAR(255)	NULL,
                           `Field2`	VARCHAR(50)	NOT NULL,
                           `Field4`	INT	NOT NULL,
                           `Field5`	VARCHAR(50)	NOT NULL,
                           `Field6`	VARCHAR(50)	NOT NULL,
                           `Field7`	VARCHAR(50)	NOT NULL,
                           `Field8`	VARCHAR(50)	NULL,
                           `Field9`	VARCHAR(50)	NOT NULL,
                           `Field10`	VARCHAR(50)	NOT NULL,
                           `Field3`	DATETIME	NOT NULL
);

CREATE TABLE `Shipping Address` (
                                    `address_id`	BIGINT	NOT NULL,
                                    `serial_number`	VARCHAR(100)	NOT NULL,
                                    `address_name`	VARCHAR(100)	NOT NULL,
                                    `recipient_name`	VARCHAR(50)	NOT NULL,
                                    `post_code`	VARCHAR(50)	NOT NULL,
                                    `road_address`	VARCHAR(50)	NOT NULL,
                                    `detail_address`	VARCHAR(50)	NOT NULL,
                                    `recipient_phone`	INT	NOT NULL
);

CREATE TABLE `Bid_result` (
                              `bid_result_id`	BIGINT	NOT NULL,
                              `order_id`	VARCHAR(50)	NULL,
                              `product_id`	VARCHAR(50)	NOT NULL,
                              `payment_status`	tinyint	NOT NULL,
                              `result`	ENUM('SUCCESS','FAILURE')	NOT NULL,
                              `end_time`	DATETIME	NOT NULL,
                              `mod_time`	DATETIME	NULL,
                              `complete_date`	DATETIME	NULL,
                              `enrolled_time`	DATETIME	NOT NULL
);

CREATE TABLE `Rating` (
                          `rating`	INT	NOT NULL,
                          `rating_datetime`	DATETIME	NOT NULL
);

CREATE TABLE `Bid_log` (
                           `product_id`	VARCHAR(50)	NOT NULL,
                           `Field`	VARCHAR(50)	NOT NULL,
                           `Field2`	INT	NOT NULL,
                           `Field3`	DATETIME	NOT NULL
);

CREATE TABLE `Untitled4` (
                             `Field`	VARCHAR(255)	NULL,
                             `Field2`	VARCHAR(255)	NULL,
                             `Field3`	VARCHAR(255)	NULL
);

CREATE TABLE `Category` (
                            `category_id`	VARCHAR(50)	NOT NULL,
                            `idol_name`	VARCHAR(100)	NOT NULL,
                            `product_type`	VARCHAR(50)	NOT NULL	COMMENT '포토카드, 응원봉, 문구류, 의류, 포토북, 앨범, DVD, 악세사리(키링, 그립톡, 에코백), 텀블러,  LP판, 시즌그리팅'
);

CREATE TABLE `User_Category` (
                                 `user_category_id`	BIGINT	NOT NULL	DEFAULT 1,
                                 `serial_number`	VARCHAR(100)	NOT NULL,
                                 `category_id`	VARCHAR(50)	NOT NULL
);

CREATE TABLE `Oauth` (
                         `Key`	VARCHAR(255)	NOT NULL,
                         `Field`	VARCHAR(255)	NULL
);

ALTER TABLE `User` ADD CONSTRAINT `PK_USER` PRIMARY KEY (
                                                         `serial_number`
    );

ALTER TABLE `Role` ADD CONSTRAINT `PK_ROLE` PRIMARY KEY (
                                                         `role_id`
    );

ALTER TABLE `Shipping` ADD CONSTRAINT `PK_SHIPPING` PRIMARY KEY (
                                                                 `shipping_id`
    );

ALTER TABLE `Subscription` ADD CONSTRAINT `PK_SUBSCRIPTION` PRIMARY KEY (
                                                                         `sub_id`
    );

ALTER TABLE `Board` ADD CONSTRAINT `PK_BOARD` PRIMARY KEY (
                                                           `board_id`
    );

ALTER TABLE `Product` ADD CONSTRAINT `PK_PRODUCT` PRIMARY KEY (
                                                               `product_id`
    );

ALTER TABLE `Report` ADD CONSTRAINT `PK_REPORT` PRIMARY KEY (
                                                             `report_id`
    );

ALTER TABLE `Reply` ADD CONSTRAINT `PK_REPLY` PRIMARY KEY (
                                                           `reply_id`
    );

ALTER TABLE `Finance` ADD CONSTRAINT `PK_FINANCE` PRIMARY KEY (
                                                               `finance_id`
    );

ALTER TABLE `Expense` ADD CONSTRAINT `PK_EXPENSE` PRIMARY KEY (
                                                               `expense_id`
    );

ALTER TABLE `Revenue` ADD CONSTRAINT `PK_REVENUE` PRIMARY KEY (
                                                               `revenue_id`
    );

ALTER TABLE `Payment` ADD CONSTRAINT `PK_PAYMENT` PRIMARY KEY (
                                                               `order_id`
    );

ALTER TABLE `Shipping Address` ADD CONSTRAINT `PK_SHIPPING ADDRESS` PRIMARY KEY (
                                                                                 `address_id`
    );

ALTER TABLE `Bid_result` ADD CONSTRAINT `PK_BID_RESULT` PRIMARY KEY (
                                                                     `bid_result_id`
    );

ALTER TABLE `Bid_log` ADD CONSTRAINT `PK_BID_LOG` PRIMARY KEY (
                                                               `product_id`
    );

ALTER TABLE `Category` ADD CONSTRAINT `PK_CATEGORY` PRIMARY KEY (
                                                                 `category_id`
    );

ALTER TABLE `User_Category` ADD CONSTRAINT `PK_USER_CATEGORY` PRIMARY KEY (
                                                                           `user_category_id`
    );

ALTER TABLE `Oauth` ADD CONSTRAINT `PK_OAUTH` PRIMARY KEY (
                                                           `Key`
    );

ALTER TABLE `User` ADD CONSTRAINT `FK_Role_TO_User_1` FOREIGN KEY (
                                                                   `role_id`
    )
    REFERENCES `Role` (
                       `role_id`
        );

ALTER TABLE `Shipping` ADD CONSTRAINT `FK_Bid_result_TO_Shipping_1` FOREIGN KEY (
                                                                                 `id2`
    )
    REFERENCES `Bid_result` (
                             `bid_result_id`
        );

ALTER TABLE `Subscription` ADD CONSTRAINT `FK_User_TO_Subscription_1` FOREIGN KEY (
                                                                                   `serial_number`
    )
    REFERENCES `User` (
                       `serial_number`
        );

ALTER TABLE `Board` ADD CONSTRAINT `FK_User_TO_Board_1` FOREIGN KEY (
                                                                     `serial_number`
    )
    REFERENCES `User` (
                       `serial_number`
        );

ALTER TABLE `Product` ADD CONSTRAINT `FK_Category_TO_Product_1` FOREIGN KEY (
                                                                             `category_id`
    )
    REFERENCES `Category` (
                           `category_id`
        );

ALTER TABLE `Product` ADD CONSTRAINT `FK_User_TO_Product_1` FOREIGN KEY (
                                                                         `serial_number`
    )
    REFERENCES `User` (
                       `serial_number`
        );

ALTER TABLE `Report` ADD CONSTRAINT `FK_User_TO_Report_1` FOREIGN KEY (
                                                                       `serial_number`
    )
    REFERENCES `User` (
                       `serial_number`
        );

ALTER TABLE `Reply` ADD CONSTRAINT `FK_Board_TO_Reply_1` FOREIGN KEY (
                                                                      `board_id`
    )
    REFERENCES `Board` (
                        `board_id`
        );

ALTER TABLE `Finance` ADD CONSTRAINT `FK_Revenue_TO_Finance_1` FOREIGN KEY (
                                                                            `revenue_id`
    )
    REFERENCES `Revenue` (
                          `revenue_id`
        );

ALTER TABLE `Finance` ADD CONSTRAINT `FK_Expense_TO_Finance_1` FOREIGN KEY (
                                                                            `expense_id`
    )
    REFERENCES `Expense` (
                          `expense_id`
        );

ALTER TABLE `Shipping Address` ADD CONSTRAINT `FK_User_TO_Shipping Address_1` FOREIGN KEY (
                                                                                           `serial_number`
    )
    REFERENCES `User` (
                       `serial_number`
        );

ALTER TABLE `Bid_result` ADD CONSTRAINT `FK_Payment_TO_Bid_result_1` FOREIGN KEY (
                                                                                  `order_id`
    )
    REFERENCES `Payment` (
                          `order_id`
        );

ALTER TABLE `Bid_result` ADD CONSTRAINT `FK_Product_TO_Bid_result_1` FOREIGN KEY (
                                                                                  `product_id`
    )
    REFERENCES `Product` (
                          `product_id`
        );

ALTER TABLE `Bid_log` ADD CONSTRAINT `FK_Product_TO_Bid_log_1` FOREIGN KEY (
                                                                            `product_id`
    )
    REFERENCES `Product` (
                          `product_id`
        );

ALTER TABLE `User_Category` ADD CONSTRAINT `FK_User_TO_User_Category_1` FOREIGN KEY (
                                                                                     `serial_number`
    )
    REFERENCES `User` (
                       `serial_number`
        );

ALTER TABLE `User_Category` ADD CONSTRAINT `FK_Category_TO_User_Category_1` FOREIGN KEY (
                                                                                         `category_id`
    )
    REFERENCES `Category` (
                           `category_id`
        );

