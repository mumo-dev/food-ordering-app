const Town = require('../sequelize').Town;
const Area = require('../sequelize').Area;

module.exports = {

    getLocation(req, res, next) {

        Town.findAll().then((towns) => {

            res.render('admin/delivery_locations', {
                towns:towns,
                success:req.flash('successMessage'),
                error: req.flash('failureMessage')
            })

        })


    },
    addTown(req, res, next) {
        const name = req.body.town;
        let message;
        if (name) {
            Town.create({name: name})
                .then(town => {
                    req.flash('successMessage', 'Town added Successfully');
                    res.redirect('/locations');
                })
                .catch(err => {
                    req.flash('failureMessage', 'Sorry,Some error occurred');
                    res.redirect('/locations');
                })

        }


    },

    updateTown(req, res) {
        const values = {name: req.body.townName};
        const selector = {
            where: {id: req.body.townId}
        };
        Town.update(values, selector)
            .then(() => {
                req.flash('successMessage', 'Town updated Successfully');
                res.redirect('/locations');
            })
            .catch(err => {

                res.redirect('/locations');
            })
    },

    deleteTown(req, res) {
        Town.findById(req.body.id)
            .then(town => {
                if (town) {
                    return town.destroy()
                }
            })
            .then(() => {
                req.flash('successMessage', 'Town deleted Successfully');
                res.redirect('/locations');
            })
            .catch((err) => {
                req.flash('errorMessage', 'Town updated Successfully');
                res.redirect('/locations');
            })
    },

    getLocationEstatesById(req, res, next) {

        Town.findById(req.params.id, {
            include: [
                {
                    model: Area,
                    as: 'areas'
                }
            ]
        }).then(town => {

            if (!town) {
                res.redirect('/locations');
            } else {
                //fetch areas in the town;;
                // Town

                res.render('admin/location_areas', {
                    town
                })
            }
        })
            .catch(err => {

            })

    },

    addArea(req, res, next) {
        // if()
        const data = {
            name: req.body.name,
            address: req.body.address,
            townId: req.body.townId
        };
        Area.create(data)
            .then(area => {
                // add flash message
                res.redirect('/locations/' + area.townId);
            })
            .catch(err => {

            })


    },
    updateArea(req, res) {
        const values = {
            name: req.body.areaName,
            address: req.body.areaAddress
        };

        const selector = {
            where: {id: req.body.areaId}
        };

        Area.update(values, selector)
            .then(() => {
                res.redirect('/locations/' + req.body.townId)
            })
    },

    deleteArea(req, res){
        const townId= req.body.townId;
        console.log(townId);
        Area.findById(req.body.id)
            .then(area => {
                if (area) {
                    return area.destroy()
                }
            })
            .then(() => {
                res.redirect('/locations/'+ townId);
            })
            .catch((err) => {

                // res.redirect('/locations');
            })
    }
};