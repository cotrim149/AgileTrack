package agiletrack

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class OwnerTaskController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond OwnerTask.list(params), model:[ownerTaskCount: OwnerTask.count()]
    }

    def show(OwnerTask ownerTask) {
        respond ownerTask
    }

    def create() {
        respond new OwnerTask(params)
    }

    @Transactional
    def save(OwnerTask ownerTask) {
        if (ownerTask == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (ownerTask.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond ownerTask.errors, view:'create'
            return
        }

        ownerTask.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'ownerTask.label', default: 'OwnerTask'), ownerTask.id])
                redirect ownerTask
            }
            '*' { respond ownerTask, [status: CREATED] }
        }
    }

    def edit(OwnerTask ownerTask) {
        respond ownerTask
    }

    @Transactional
    def update(OwnerTask ownerTask) {
        if (ownerTask == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (ownerTask.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond ownerTask.errors, view:'edit'
            return
        }

        ownerTask.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'ownerTask.label', default: 'OwnerTask'), ownerTask.id])
                redirect ownerTask
            }
            '*'{ respond ownerTask, [status: OK] }
        }
    }

    @Transactional
    def delete(OwnerTask ownerTask) {

        if (ownerTask == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        ownerTask.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'ownerTask.label', default: 'OwnerTask'), ownerTask.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'ownerTask.label', default: 'OwnerTask'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
