package agiletrack

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class InvestmentThemeController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond InvestmentTheme.list(params), model:[investmentThemeCount: InvestmentTheme.count()]
    }

    def show(InvestmentTheme investmentTheme) {
        respond investmentTheme
    }

    def create() {
        respond new InvestmentTheme(params)
    }

    @Transactional
    def save(InvestmentTheme investmentTheme) {
        if (investmentTheme == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (investmentTheme.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond investmentTheme.errors, view:'create'
            return
        }

        investmentTheme.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'investmentTheme.label', default: 'InvestmentTheme'), investmentTheme.id])
                redirect investmentTheme
            }
            '*' { respond investmentTheme, [status: CREATED] }
        }
    }

    def edit(InvestmentTheme investmentTheme) {
        respond investmentTheme
    }

    @Transactional
    def update(InvestmentTheme investmentTheme) {
        if (investmentTheme == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (investmentTheme.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond investmentTheme.errors, view:'edit'
            return
        }

        investmentTheme.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'investmentTheme.label', default: 'InvestmentTheme'), investmentTheme.id])
                redirect investmentTheme
            }
            '*'{ respond investmentTheme, [status: OK] }
        }
    }

    @Transactional
    def delete(InvestmentTheme investmentTheme) {

        if (investmentTheme == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        investmentTheme.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'investmentTheme.label', default: 'InvestmentTheme'), investmentTheme.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'investmentTheme.label', default: 'InvestmentTheme'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
