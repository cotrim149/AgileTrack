package agiletrack

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class AcceptanceCriteriaController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond AcceptanceCriteria.list(params), model:[acceptanceCriteriaCount: AcceptanceCriteria.count()]
    }

    def show(AcceptanceCriteria acceptanceCriteria) {
        respond acceptanceCriteria
    }

    def create() {
        respond new AcceptanceCriteria(params)
    }

    @Transactional
    def save(AcceptanceCriteria acceptanceCriteria) {
        if (acceptanceCriteria == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (acceptanceCriteria.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond acceptanceCriteria.errors, view:'create'
            return
        }

        acceptanceCriteria.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'acceptanceCriteria.label', default: 'AcceptanceCriteria'), acceptanceCriteria.id])
                redirect acceptanceCriteria
            }
            '*' { respond acceptanceCriteria, [status: CREATED] }
        }
    }

    def edit(AcceptanceCriteria acceptanceCriteria) {
        respond acceptanceCriteria
    }

    @Transactional
    def update(AcceptanceCriteria acceptanceCriteria) {
        if (acceptanceCriteria == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (acceptanceCriteria.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond acceptanceCriteria.errors, view:'edit'
            return
        }

        acceptanceCriteria.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'acceptanceCriteria.label', default: 'AcceptanceCriteria'), acceptanceCriteria.id])
                redirect acceptanceCriteria
            }
            '*'{ respond acceptanceCriteria, [status: OK] }
        }
    }

    @Transactional
    def delete(AcceptanceCriteria acceptanceCriteria) {

        if (acceptanceCriteria == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        acceptanceCriteria.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'acceptanceCriteria.label', default: 'AcceptanceCriteria'), acceptanceCriteria.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'acceptanceCriteria.label', default: 'AcceptanceCriteria'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
