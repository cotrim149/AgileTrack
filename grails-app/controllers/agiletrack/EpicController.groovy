package agiletrack

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class EpicController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Epic.list(params), model:[epicCount: Epic.count()]
    }

    def show(Epic epic) {
        respond epic
    }

    def create() {
        respond new Epic(params)
    }

    @Transactional
    def save(Epic epic) {
        if (epic == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (epic.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond epic.errors, view:'create'
            return
        }

        epic.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'epic.label', default: 'Epic'), epic.id])
                redirect epic
            }
            '*' { respond epic, [status: CREATED] }
        }
    }

    def edit(Epic epic) {
        respond epic
    }

    @Transactional
    def update(Epic epic) {
        if (epic == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (epic.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond epic.errors, view:'edit'
            return
        }

        epic.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'epic.label', default: 'Epic'), epic.id])
                redirect epic
            }
            '*'{ respond epic, [status: OK] }
        }
    }

    @Transactional
    def delete(Epic epic) {

        if (epic == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        epic.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'epic.label', default: 'Epic'), epic.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'epic.label', default: 'Epic'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
