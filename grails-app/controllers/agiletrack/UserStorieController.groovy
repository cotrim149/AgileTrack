package agiletrack

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class UserStorieController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond UserStorie.list(params), model:[userStorieCount: UserStorie.count()]
    }

    def show(UserStorie userStorie) {
        respond userStorie
    }

    def create() {
        respond new UserStorie(params)
    }

    @Transactional
    def save(UserStorie userStorie) {
        if (userStorie == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (userStorie.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond userStorie.errors, view:'create'
            return
        }

        userStorie.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'userStorie.label', default: 'UserStorie'), userStorie.id])
                redirect userStorie
            }
            '*' { respond userStorie, [status: CREATED] }
        }
    }

    def edit(UserStorie userStorie) {
        respond userStorie
    }

    @Transactional
    def update(UserStorie userStorie) {
        if (userStorie == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (userStorie.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond userStorie.errors, view:'edit'
            return
        }

        userStorie.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'userStorie.label', default: 'UserStorie'), userStorie.id])
                redirect userStorie
            }
            '*'{ respond userStorie, [status: OK] }
        }
    }

    @Transactional
    def delete(UserStorie userStorie) {

        if (userStorie == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        userStorie.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'userStorie.label', default: 'UserStorie'), userStorie.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'userStorie.label', default: 'UserStorie'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
