INSERT INTO exchange_request (user_id, requested_card_id, status) VALUES
(1, 3, 'PENDING'),
(2, 5, 'PENDING'),
(3, 4, 'PENDING'),
(1, 4, 'PENDING'),
(1, 5, 'PENDING'),
(3, 3, 'ACCEPTED'),
(4, 2, 'PENDING');

INSERT INTO exchange_offer (bidder_id, exchange_request_id, offered_card_id, status) VALUES
(1, 2, 2, 'PENDING');

INSERT INTO exchange_counteroffer(offered_card_id, status, exchange_request_id, exchange_offer_id) VALUES
(3,'PENDING',2,1)