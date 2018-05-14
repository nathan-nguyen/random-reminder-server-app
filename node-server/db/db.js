module.exports = function (){

	var db = {};
	var Nedb = require('nedb');
	var post = new Nedb({ filename: './db/post.db', autoload: true });

	db.queryPost = function(id, cb) {
		post.find({ _id: id }, function (err, docs) {
			if (err) {
				console.log(err);
				return cb(err);
			}

			if (docs.length > 0) {
				console.log("Content for post id " + id + ": " + docs[0].content);
			}
			else {
				console.log("Content for post id " + id + " does not exist in db");
			}
			return cb(err, docs);
		});
	}

	return db;
}
