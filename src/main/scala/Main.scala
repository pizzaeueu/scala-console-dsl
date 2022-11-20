import cats.effect.{ExitCode, IO, IOApp}
import com.consolebuilder.renderer.Renderer
import cats.effect.kernel.Sync
import cats.implicits.*
import cats.Monad
import cats.data.StateT
import cats.data.StateT.*
import cats.effect.kernel.Ref
import eu.timepit.refined.types.string.NonEmptyString
import com.consolebuilder.domain.TextMessage
import com.consolebuilder.Dsl
import com.consolebuilder.domain.*
import com.consolebuilder.domain.InteractiveMessage.*
import org.w3c.dom.Text

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    val renderer = Renderer.of[IO]()
    
    def interpretator(list: IO[List[InteractiveMessage]]): IO[Unit] = {
      val map: scala.collection.mutable.Map[String, String] = scala.collection.mutable.Map.empty
      Monad[IO].tailRecM[IO[List[InteractiveMessage]], Unit](list) { state1 => 
        state1.flatMap { state =>
          state match {
           case e :: other if e.isInstanceOf[TextMessage] => 
              val updatedOutput = 
                map.foldLeft[String](e.asInstanceOf[TextMessage].s.value) { case (result, (key, value)) =>
                  result.replace(key, value)
                }.toTextMessageUnsafe

              renderer.render(updatedOutput) *> IO.pure(Left(IO(other)))
            
           case e :: other if e.isInstanceOf[UserResponse] => 
              renderer.readFromConsole.map(str => map.addOne(e.asInstanceOf[UserResponse].id -> str)) *> IO.pure(Left(IO(other)))

           case Nil => IO.pure(Right(IO(List.empty)))

           case _ => throw RuntimeException(state.toString)
          }
        }
        
      }
    }

    val dsl = Dsl.of[IO]()
    
    val p2 = for
      _    <- dsl.writeMessage("Write Your Name".toTextMessageUnsafe)
      name <- dsl.readMessage()
      _    <- dsl.writeMessage(s"Hello, $name".toTextMessageUnsafe)
    yield ()

    val out = p2.run(List.empty).map(_._1)
    interpretator(out).as(ExitCode.Success)
}
