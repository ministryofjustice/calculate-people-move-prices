package uk.gov.justice.digital.hmpps.pecs.jpc.auditing

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import uk.gov.justice.digital.hmpps.pecs.jpc.service.AuditService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LogOutAuditHandler : LogoutSuccessHandler {
  @Autowired
  private lateinit var auditService: AuditService

  @Value("\${HMPPS_AUTH_BASE_URI}")
  private lateinit var authLogoutSuccessUri: String

  override fun onLogoutSuccess(
    request: HttpServletRequest?,
    response: HttpServletResponse?,
    authentication: Authentication?
  ) {
    authentication?.let { auditService.createLogOutEvent(it.name) }
    response?.sendRedirect(authLogoutSuccessUri)
  }
}
