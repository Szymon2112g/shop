insert into thing (id, name, price, quantity)
values(1,'Butter', 1, 10);
insert into thing (id, name, price, quantity)
values(2,'Chicken', 10, 20);
insert into thing (id, name, price, quantity)
values(3,'Bread', 3, 30);


insert into orders (id, customer)
values(1,'Szymon Godzinski');
insert into orders (id, customer)
values(2,'Szymon Godzinski2');

insert into thing_orders (order_id, thing_id)
values(1,1);
insert into thing_orders (order_id, thing_id)
values(2,2);

