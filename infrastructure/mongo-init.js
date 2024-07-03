db.auth('root', 'root')
db = db.getSiblingDB('note_app');
db.createUser({
    user: "note_user",
    pwd: "note_pass",
    roles: [{ role: "readWrite", db: "note_app" }]
});