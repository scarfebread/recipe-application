package thecookingpot.oauth.repository

import org.springframework.stereotype.Service
import thecookingpot.oauth.model.Actor

@Service
class ActorRepository {
    private val actors = mutableListOf<Actor>()

    fun findActorByState(state: String): Actor {
        return actors.first { actor -> actor.state == state }
    }

    fun createActor(): Actor {
        Actor().let { actor ->
            actors.add(actor)
            return actor
        }
    }
}