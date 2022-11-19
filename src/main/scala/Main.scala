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

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    val renderer = Renderer.of[IO]()
    
    def loop2(list: IO[List[InteractiveMessage]]): IO[Unit] = {
      Monad[IO].tailRecM[IO[List[InteractiveMessage]], Unit](list) { state1 => 
        state1.flatMap { state =>
          state match {
           case e :: other => 
              renderer.render(e) *> IO.pure(Left(IO(other)))
           case Nil => IO.pure(Right(IO(List.empty)))
          }
        }
        
      }
    }

    val dsl = Dsl.of[IO]()
    
    val p2 = for
      _ <- dsl.writeMessage("Hello".toTextMessageUnsafe)
      _ <- dsl.writeMessage("World!".toTextMessageUnsafe)
    yield ()

    val out = p2.run(List.empty).map(_._1)
    loop2(out).as(ExitCode.Success)
}
