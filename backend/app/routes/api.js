var express = require('express');
var menuController = require('../controllers/api/menu');
var router = express.Router();

router.get('/menus', menuController.findAllMenus);
router.get('/menus/location/:town_id', menuController.findAllMenusInTown);
router.get('/menus/:id', menuController.findMenuById);

router.get('/towns', menuController.findTowns);
router.get('/towns/:id', menuController.findTownById);


module.exports = router;