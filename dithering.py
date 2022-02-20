import os
from argparse import ArgumentParser
from math import floor
from PIL import Image


def floyd_steinberg_dither(image_file):
    """ Grayscale Floyd Steinberg Dither
    """
    if not os.path.exists(image_file):
            raise FileNotFoundError(path)

    apply_threshold = lambda value:  255 * floor(value/128)

    new_img = Image.open(image_file)
    new_img = new_img.convert('LA')
    pixel = new_img.load()

    x_lim, y_lim = new_img.size

    for y in range(1, y_lim):
        for x in range(1, x_lim):
            black_oldpixel, white_oldpixel = pixel[x, y]

            black_newpixel = apply_threshold(black_oldpixel)
            white_newpixel = apply_threshold(white_oldpixel)

            pixel[x, y] = black_newpixel, white_newpixel

            black_error = black_oldpixel - black_newpixel
            white_error = white_oldpixel - white_newpixel

            if x < x_lim - 1:
                black = pixel[x+1, y][0] + round(black_error * 7/16)
                white = pixel[x+1, y][1] + round(white_error * 7/16)
                pixel[x+1, y] = (black, white)

            if x > 1 and y < y_lim - 1:
                black = pixel[x-1, y+1][0] + round(black_error * 3/16)
                white = pixel[x-1, y+1][1] + round(white_error * 3/16)
                pixel[x-1, y+1] = (black, white)

            if y < y_lim - 1:
                black = pixel[x, y+1][0] + round(black_error * 5/16)
                white = pixel[x, y+1][1] + round(white_error * 5/16)

                pixel[x, y+1] = (black, white)

            if x < x_lim - 1 and y < y_lim - 1:
                black = pixel[x+1, y+1][0] + round(black_error * 1/16)
                white = pixel[x+1, y+1][1] + round(white_error * 1/16)

                pixel[x+1, y+1] = (black, white)

    return new_img



if  __name__ == '__main__':
    parser = ArgumentParser(description="Image dithering")
    parser.add_argument("image_path", help="input image location")
    parser.add_argument("-o", default=None, help="output image location")

    args = parser.parse_args()
    # new_img = Dither(path=args.image_path, output=args.o).floyd_steinberg_dither(True)

    # if args.o:
    #     new_img.save(output)
    # else:
    #     new_img.show()


    new_img = floyd_steinberg_dither(args.image_path)
    new_img.show()