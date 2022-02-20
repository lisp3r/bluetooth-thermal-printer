import os
import sys
import codecs
import json
import six
import time
from bluepy import btle
from textwrap import wrap


class Printer:

    def __init__(self, printer_mac='B7:26:A2:0D:CA:66', mtu=123):
        self.MTU = mtu
        self.MAC_ADDR = printer_mac
        self.printer = None

    def init_printer(self):
        if not self.printer:
            self.printer = btle.Peripheral(deviceAddr=self.MAC_ADDR)
            self.printer.setMTU(self.MTU)

    def send_image(self, image):
        self.init_printer()

        # Send technical info
        self.printer.writeCharacteristic(6, codecs.decode('5178a80001000000ff5178a30001000000ff', "hex"))
        self.printer.writeCharacteristic(6, codecs.decode('5178bb0001000107ff', "hex"))

        # Send image
        image = wrap(image, 112)
        for line in image:
            printer.writeCharacteristic(6, codecs.decode(line, 'hex'))
            time.sleep(0.1)

        self.printer.disconnect()


if __name__ == '__main__':
    try:
        i_file = sys.argv[1]
        if not os.path.isfile(i_file):
            raise IndexError
    except IndexError:
        print(f'[E]', 'Usage: {sys.argv[0]} [wireshark_json_dump]')
        exit(1)

