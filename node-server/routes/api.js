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

var path = require('path');

const dbModule = require('../db/db');
const db = new dbModule();

router.use(express.static('public'));

router.route('/post/:_id').get(function (req, res) {
        db.queryPost(req.params._id, function(err, docs) {
		if (err){
			logger.error(err);
			res.sendFile(path.join(__dirname + '/../html/500.html'));
		}
		else {
			if (docs) {
				res.sendFile(path.join(__dirname + docs));
			}
			else {
				res.sendFile(path.join(__dirname + '/../html/404.html'));
			}
		}
	});
});

router.route('/tag/:_id').get(function (req, res) {
        db.queryTag(req.params._id, function(err, docs) {
                if (err){
                        logger.error(err);
                        res.sendFile(path.join(__dirname + '/../html/500.html'));
                }   
                else {
                        if (docs) {
				console.log(docs);
				var s = "<html><body><h3>" + req.params._id + "</h3>";
				for (var i = 0; i < docs.length; i++) {
					s += '<a href="/post/' + docs[i] + '">' + docs[i] + '</a><br/>';
				}
				s += "</body></html>";
				
				res.send(s);
                        }   
                        else {
                                res.sendFile(path.join(__dirname + '/../html/404.html'));
                        }   
                }   
        }); 
});

router.route('/random/').get(function (req, res) {
        db.queryRandomPost(function(err, docs) {
                if (err){
                        logger.error(err);
                        res.sendFile(path.join(__dirname + '/../html/500.html'));
                }
                else {
                        if (docs) {
                                res.sendFile(path.join(__dirname + docs));
                        }
                        else {
                                res.sendFile(path.join(__dirname + '/../html/404.html'));
                        }
                }
        });
});

module.exports = router;
