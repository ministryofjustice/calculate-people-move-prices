package uk.gov.justice.digital.hmpps.pecs.jpc.move

import org.springframework.data.repository.CrudRepository

interface JourneyRepository : CrudRepository<Journey, String> {

}