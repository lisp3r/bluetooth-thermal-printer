import codecs, json, six
from bluepy import btle


printer_mac_addp = 'B7:26:A2:0D:CA:66'


# class NotifyHandler(btle.DefaultDelegate):
#     def __init__(self, params=None):
#         std.debug('Initialize notification handler')
#         btle.DefaultDelegate.__init__(self)

#     def handleNotification(self, cHandle, data):
#         std.debug('Got ntification')
#         print(cHandle)
#         print(data)


# todo: singleton
class STD():
    def __str(self, msg, type):
        print('[{}] {}'.format(type, msg))

    def debug(self, msg):
        self.__str(msg, 'D')

    def info(self, msg):
        self.__str(msg, 'I')

    def warn(self, msg):
        self.__str(msg, 'W')

    def err(self, msg):
        self.__str(msg, 'E')


std = STD()


def get_all_services(printer):
    services = []
    _services = printer.getServices()
    for _s in _services:
        if s := printer.getServiceByUUID(s.uuid.getCommonName()):
            services.append(s)

    return services


def init_item(s):
    if not s.uuid.commonName:
        s.uuid.commonName = s.uuid.getCommonName()
    if hasattr(s, 'chars') and not s.chars:
        s.getCharacteristics()
        for ch in s.chars:
            init_item(ch)
    if hasattr(s, 'descs') and not s.descs:
        s.getDescriptors()
        for d in s.descs:
            init_item(d)


def print_info(peripheral):
    pass




try:
    std.info('Trying to connected to {}...'.format(printer_mac_addp))
    printer = btle.Peripheral(deviceAddr=printer_mac_addp)
    std.info('Connected to {}'.format(printer_mac_addp))
except btle.BTLEDisconnectError as err:
    std.err(err)
    exit(1)

printer.setMTU(123)

try:
    std.info('Searching for "ae30" service...')
    ae30_service = printer.getServiceByUUID('ae30')
except btle.BTLEEException:
    std.err('Can not find service with UUID ae30')
    std.warn('Services: {}'.format(str(get_all_services(printer))))
    exit(1)

# init_item(ae30_service)

# msg = 'Found service ae30 with characteristics:\n\n'
# for ch in ae30_service.chars:
#     msg += '    Characteristic 0x{} ({})\n'.format(ch.uuid.getCommonName().upper(), ch.propertiesToString())
#     if ch.descs:
#         msg += '        Characteristic Configuration:\n'
#         msg += ''.join(['        UUID: {}, Handle: {}\n'.format(x.uuid, x.handle) for x in ch.descs])

std.info(msg)

# printer.setDelegate(NotifyHandler)

# ae02_char = [ch for ch in ae30_service.chars if ch.uuid.getCommonName() == 'ae02']
# if ae02_char:
#     ae02_char = ae02_char[0]
# else:
#     std.err('No characteristic with UUID "ae02"')

# notification_data = b"0100"


# print(ae02_char.descs[0].__dict__)
# print(ae02_char.descs[0].write(notification_data))

# std.info('Turning on notifications from ae02 characteristic')

# ae02_char.write(notification_data)

# while True:
#     if printer.waitForNotifications(1.0):
#         continue

#     print ("Waiting...")
    # Perhaps do something else here

# Handle: 0x0009 (Unknown: Unknown: Client Characteristic Configuration)
#     [Service UUID: Unknown (0xae30)]
#     [Characteristic UUID: Unknown (0xae02)]
#     [UUID: Client Characteristic Configuration (0x2902)]
#     Characteristic Configuration Client: 0x0001, Notification
#         0000 0000 0000 00.. = Reseved: 0x0000
#         .... .... .... ..0. = Indication: False
#         .... .... .... ...1 = Notification: True

# Setup to turn notifications on, e.g.
#   svc = p.getServiceByUUID( service_uuid )
#   ch = svc.getCharacteristics( char_uuid )[0]
#   ch.write( setup_data )


#printer.writeCharacteristic(6, codecs.decode('5178a80001000000ff5178a30001000000ff', 'hex'))
#printer.writeCharacteristic(6, codecs.decode('5178bb0001000107ff', 'hex'))



std.info('Disonnected from {}'.format(printer_mac_addp))
printer.disconnect()

# std.info('Trying to read from aeo2')

# for ch in ae30.chars:
#     try:
#         if ch.supportsRead():
#             std.info('{} supports read. Trying...'.format(ch.uuid))
#             print(ch.read())
#     except btle.BTLEInternalError as err:
#         std.err(err)


# print(bytes.fromhex(cat))
# print(codecs.decode(cat, "hex").decode('utf-8'))
