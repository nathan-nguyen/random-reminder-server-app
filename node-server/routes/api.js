var express = require('express');
var router = express.Router();
var bodyParser = require('body-parser');
router.use(bodyParser.json());

var winston = require('winston');                                                               //logger module
var logger = new (winston.Logger)({
        level: 'debug',
        transports: [
                new (winston.transports.Console)({ colorize: true }),
        ]
});

router.route('/api/post/:_id').get(function (req, res) {
	var returnRes = {
                code: 1,
                msg: '',
                result: {},
        }

	// Intialize result
	var result = {status:"OK"};

        returnRes.msg = 'Post id ' + req.params._id;
        returnRes.result = result;

        res.json(returnRes);
});

module.exports = router;
