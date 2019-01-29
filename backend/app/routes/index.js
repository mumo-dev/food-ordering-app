var express = require('express');
var passport = require('passport');
var sharp = require('sharp');

var multer = require('multer');

var storage = multer.diskStorage({
    destination: function (req, file, cb) {
        cb(null, 'public/images/')
    },
    filename: function (req, file, cb) {
       /* sharp().resize(200, 200).toFile(newPath, function(err) {
            if (err) {
                throw err;
            }
        });*/
        if (req.body.menuImageUrl){
            cb(null,  req.body.menuImageUrl);
            return;
        }
        cb(null,  Date.now()+ '-'+ file.originalname);
    }
});

var upload = multer({
    storage: storage
});



var adminController = require('../controllers/admin');
var restaurantController = require('../controllers/restaurant');
var router = express.Router();

/* GET home page. */
router.get('/', function (req, res, next) {
    res.render('index', {title: 'Express'});
});

router.get('/login', (function (req, res) {
    res.render('login', {message: req.flash('loginMessage')});
}));

router.post('/login', passport.authenticate('local-signin', {
    successRedirect: '/dashboard',
    failureRedirect: '/login',
    failureFlash: true
}), function (req, res) {
    // if (req.body.remember) {
    req.session.cookie.maxAge = 24 * 60 * 60 * 1000;// 24 hours
    // }
    res.redirect('/');
});

router.get('/signup', function (req, res) {
    res.render('signup', {message: req.flash('signupMessage')});
});

router.post('/signup', passport.authenticate('local-signup', {
    successRedirect: '/dashboard',
    failureRedirect: '/signup',
    failureFlash: true
}), function (req, res) {
    console.log(req.body)
});

router.get('/dashboard', isLoggedIn, function (req, res) {
    res.render('dashboard', {
        user: req.user
    });

});

/* TODO add isLoggedIn middleware */
router.get('/locations', isLoggedIn, adminController.getLocation);
router.post('/addTown', isLoggedIn, adminController.addTown);
router.post('/updateTown', isLoggedIn, adminController.updateTown);
router.post('/deleteTown', isLoggedIn, adminController.deleteTown);

router.get('/locations/:id', isLoggedIn, adminController.getLocationEstatesById);
router.post('/addArea', adminController.addArea);
router.post('/updateArea', adminController.updateArea);
router.post('/deleteArea', adminController.deleteArea);

router.get('/restaurants', restaurantController.index);
router.get('/restaurants/:id', restaurantController.displayMenu);
router.post('/addRestaurant', restaurantController.add);
router.post('/updateRestaurant', restaurantController.update);
router.post('/deleteRestaurant', restaurantController.delete);

router.post('/addMenu', upload.single('menuImage'), restaurantController.addMenu);
router.post('/updateMenu', upload.single('menuImage'), restaurantController.updateMenu);
router.post('/deleteMenu', restaurantController.deleteMenu);


router.get('/logout', function (req, res) {
    req.logout();
    res.redirect('/');
});


// route middleware to make sure
function isLoggedIn(req, res, next) {

    // if user is authenticated in the session, carry on
    if (req.isAuthenticated())
        return next();

    // if they aren't redirect them to the home page
    res.redirect('/');
}

module.exports = router;
