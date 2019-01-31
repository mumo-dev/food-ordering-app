var express = require('express');
var menuController = require('../controllers/api/menu');
var orderController = require('../controllers/api/order');
var checkAuth = require('../middleware/check_auth');
var router = express.Router();

router.get('/menus', menuController.findAllMenus);
router.get('/menus/location/:town_id', menuController.findAllMenusInTown);
router.get('/menus/:id', menuController.findMenuById);

router.get('/towns', menuController.findTowns);
router.get('/towns/:id', menuController.findTownById);

router.post('/orders',checkAuth,orderController.saveOrder);
router.get('/orders',orderController.findAllOrders);
router.get('/orders/:id', checkAuth,orderController.findOrderById);
router.get('/orders/users/:id', checkAuth, orderController.findOrderByUser);

module.exports = router;