const User = require('../../sequelize').User;
const Order = require('../../sequelize').Order;
const OrderItems = require('../../sequelize').OrderItems;
const sequelize = require('sequelize');


module.exports = {

    saveOrder(req, res) {
        /*
        {
          user_id:1,
          items:[{
          menu_id,
          price
        }]
         */
        let orderItems = req.body.items;
        Order.create({
            userId: req.body.userId,
            status: 'placed',
            deliveryCost: req.body.cost
        }).then(order => {

            console.log(orderItems);
            orderItems.forEach(function (item) {
                item['orderId'] = order.id
            });
            console.log(orderItems);
            OrderItems.bulkCreate(orderItems)
                .then(itemsOrdered => {
                    return res.status(200).json({
                        message: 'Order Placed successfully',
                        items: itemsOrdered
                    })
                })
                .catch(err => {
                    return res.status(500).json({
                        error: err,
                        message: 'Oops!, something went wrong.'
                    })
                })


        }).catch(err => {
            return res.status(500).json({
                error: err,
                message: 'Oops!, something went wrong.'
            })
        })

    },

    findAllOrders(req, res){
        Order.findAll({
            include:[OrderItems]
        }).then(orders=>{
            return res.status(200).json(orders)
        }).catch(err=>{
            return res.status(500).json({
                error: err
            })
        })
    },

    findOrderById(req, res){
        Order.findById(req.params.id,{
            include:[OrderItems]
        }).then(orders=>{
            return res.status(200).json(orders)
        }).catch(err=>{
            return res.status(500).json({
                error: err
            })
        })
    },

    findOrderByUser(req, res){
        Order.findAll({
            include:[OrderItems],
            where:{
                userId: req.params.id
            }
        }).then(orders=>{
            return res.status(200).json(orders)
        }).catch(err=>{
            return res.status(500).json({
                error: err
            })
        })
    }

};