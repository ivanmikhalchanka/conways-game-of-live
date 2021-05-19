package factory.game;

import model.CachingBoard;
import renderer.BoardRenderer;
import service.emulator.ComputationDelayEmulator;
import service.game.FutureGameService;

public interface FutureGameServiceFactory {
  FutureGameService build();
}
