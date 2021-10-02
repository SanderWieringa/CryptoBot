class Auth {
    constructor() {
        this.authenticated = false
    }

    register(cb) {
        this.authenticated = false
        cb()
    }

    login(cb) {
        this.authenticated = true
        cb()
    }

    logout(cb) {
        this.authenticated = false
        cb()
    }

    isAuthenticated() {
        return this.authenticated
    }
}

export default new Auth()