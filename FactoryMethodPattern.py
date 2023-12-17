from abc import ABC, abstractmethod


class PenFactory(ABC):

    def exec_business_logic(self) -> None:
        pen_obj: 'Pen' = self.make_pen()
        print(pen_obj)

    @abstractmethod
    def make_pen(self) -> 'Pen': ...


class Pen(ABC): ...


class BallpointPen(Pen):
    def __str__(self) -> str:
        return 'BallpointPen'


class FeatherPen(Pen):
    def __str__(self) -> str:
        return 'FeatherPen'


class BallPointPenManufacturer(PenFactory):
    def make_pen(self) -> 'Pen':
        return BallpointPen()


class FeatherPenManufacturer(PenFactory):
    def make_pen(self) -> 'Pen':
        return FeatherPen()


def main() -> None:
    bpoint_pen_manufacturer: PenFactory = BallPointPenManufacturer()
    bpoint_pen_manufacturer.exec_business_logic()

    feather_pen_manufacturer: PenFactory = FeatherPenManufacturer()
    feather_pen_manufacturer.exec_business_logic()


main()
