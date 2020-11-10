package uk.gov.justice.digital.hmpps.pecs.jpc.filter

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.io.IOException
import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ChooseSupplierFilter : Filter {
    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig?) {
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val req = request as HttpServletRequest
        val res = response as HttpServletResponse
        val session = req.getSession()
        val supplier = session.getAttribute("supplier")

        if (supplier == null) {
            res.sendRedirect("/choose-supplier");
        } else {
            chain.doFilter(request, response)
        }
    }

    override fun destroy() {
    }
}