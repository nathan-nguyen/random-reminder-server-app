module.exports = function (){

	var db = {};
	var Nedb = require('nedb');
	var post = new Nedb({ filename: './db/post.db', autoload: true });
	var tag = new Nedb({ filename: './db/tag.db', autoload: true });

	db.queryPost = function(id, cb) {
		post.find({ _id: id }, function (err, docs) {
			if (err) {
				console.log(err);
				return cb(err);
			}

			if (docs.length === 0) {
				console.log("Content for post id " + id + " does not exist in db");
				return cb(err);
			}
			console.log("Content for post id " + id + ": " + docs[0].content);
			return cb(err, docs[0].content);
		});
	}

	db.queryTag = function(id, cb) {
                tag.find({ _id: id }, function (err, docs) {
                        if (err) {
                                console.log(err);
                                return cb(err);
                        }

                        if (docs.length === 0) {
                                console.log("Content for tag id " + id + " does not exist in db");
				return cb(err);
                        }

			console.log("Content for tag id " + id + ": " + docs[0].content);
                        return cb(err, docs[0].content);
                });
        }

	db.queryRandomPost = function(cb) {
                post.find({}, function (err, docs) {
                        if (err) {
                                console.log(err);
                                return cb(err);
                        }

                        if (docs.length === 0) {
                                console.log("There is no post in db");
				return cb(err);
                        }
                        return cb(err, docs[Math.floor(Math.random() * docs.length)].content);
                });
        }

	return db;
}
