var express = require('express');
var passport = require('passport');
var adminController = require('../controllers/admin')
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
    if (req.body.remember) {
        req.session.coookie.maxAge = 1000 * 60 * 3;
    }
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

router.get('/locations', isLoggedIn, adminController.location);

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
