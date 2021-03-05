package thecookingpot.oauth.repository

import org.springframework.stereotype.Service
import thecookingpot.oauth.model.Actor

// TODO convert to JPA (the_cooking_pot.oauth)
@Service
class ActorRepository {
    private val actors = mutableListOf<Actor>()

    fun findActorByState(state: String): Actor {
        return actors.first { actor -> actor.state == state }
    }

    // TODO I should probably have a more robust method of finding a session than the email address
    fun findByEmailAddress(email: String): Actor {
        return actors.first { actor -> actor.token.idToken.email == email }
    }

    fun createActor(): Actor {
        Actor().let { actor ->
            actors.add(actor)
            return actor
        }
    }
}