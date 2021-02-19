package thecookingpot.oauth.repository

import org.springframework.stereotype.Service
import thecookingpot.oauth.model.Actor

@Service
class ActorRepository {
    private val actors = listOf<Actor>()

    fun getActorByState(state: String): Actor {
        return actors.first { actor -> actor.state == state }
    }

    fun createActor(): Actor {
        return Actor()
    }
}