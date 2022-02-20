from escpos import printer

image_src = 'IMG_20210604_001251__01__01__01.jpg'
printer = printer.Dummy()

printer.image(image_src)
# p._output_list