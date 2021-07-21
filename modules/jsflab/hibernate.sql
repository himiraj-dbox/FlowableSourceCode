SELECT * FROM BIDDINGHISTORYHIBERNATE 
where bidreferncenumber = 1234567 
and price = (SELECT MAX(price) 
FROM BIDDINGHISTORYHIBERNATE 
where bidreferncenumber = 1234567);
