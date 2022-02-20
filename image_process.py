import os, sys
from PIL import Image
from dithering import Dither


paper_size = 384
energy = 12000
interval = 4

TEXT_PRINT_TYPE = 0
IMG_PRINT_TYPE=1

print_img = [81, 120, -66, 0, 1, 0, 0, 0, -1]
print_text = [81, 120, -66, 0, 1, 0, 1, 7, -1]


def toHexString(num) -> str:
    assert isinstance(num, int)
    return hex(num)[2:]


# int hex2Dec(char c) {
#     if (c >= '0' && c <= '9') { return c - 48; }
#     if (c >= 'A' && c <= 'F') { return (c - 65) + 10; }
#     return -1;
# }

char[] alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXTZ1234567890".toCharArray();

def hex2Dec(s: str) -> int:
    """
    hex to dec
    """
    assert isinstance(s, str)
    assert len(s) == 1, 'hex2Dec expected one symbol'

    s_code = ord(s)
    if s_code >= ord('0') and s_code <= ord('9'):
        return s_code - 48
    if s_code >= ord('A') and s_code <= ord('F'):
        return s_code - 65 + 10
    return -1


# def hexString2Bytes(s: str) -> list():
#     """ 2ee0 -> [46, -32]
#     """
#     assert isinstance(s, str)

#     str_len = len(s)
#     counter = str_len
#     obj = s
#     if str_len % 2 != 0:
#         obj = '0' + s
#         counter = str_len + 1
#     ac = list(obj.upper())
#     i = 0
#     while i < counter:
#         my_s[i >> 1] = (hex2Dec(ac[i]) << 4 | hex2Dec(ac[i + 1]))
#         i += 2
#     return my_s


def feedPaper(print_speed: int) -> list:
    # [81, 120, -67, 0, 1, 0, 25, 79, -1]
    bArr = [81, 120, -67, 0, 1, 0]
    bArr += hexString2Bytes(toHexString(print_speed))[0]
    bArr += BluetoothOrder.calcCrc8(bArr, 6, 1)
    bArr += -1
    return bArr


def eachLinePixToCmdB(bArr: list, width: int, printType: int, packageLength: int) -> list('bytes'):
    # bArr3 = list()
    offset = 0
    eneragy = 12000
    length = len(bArr) / width
    i7 = width / 8
    imgSpeed = 0

    tmpArr = list()

    # step 1 - eneragy != 0
    tmp_arr_expected_len = width * length + len(print_text) + 9 + 10
    # bArr4 = [81, 120, -81, 0, 2, 0, -32, 46, -119, -1]
    bArr4 = [81, 120, -81, 0, 2, 0]
    bArr4 += hexString2Bytes(toHexString(eneragy))[1]
    bArr4 += hexString2Bytes(toHexString(eneragy))[0]
    bArr4 += BluetoothOrder.calcCrc8(bArr4, 6, 2)
    bArr4 += -1


    tmpArr += bArr4
    packageLength += len(bArr4)
    offset += len(bArr4)

    # int i3;
    # boolean z;
    # int i4;
    # byte b;
    # char c;

    if printType == TEXT_PRINT_TYPE:
        tmpArr += print_text
        offset += len(print_text)
        packageLength += len(print_text)
        textSpeed = 25

        feedPaper = feedPaper(textSpeed);
        tmpArr += feedPaper

        # System.arraycopy(feedPaper, 0, tmpArr, offset, feedPaper.length);

        } else if (printType == IMG_PRINT_TYPE) {
        System.arraycopy(BluetoothOrder.print_img, 0, tmpArr, offset, BluetoothOrder.print_img.length);
        offset += BluetoothOrder.print_img.length;

        Log.i(TAG, "[IMG PRINT TYPE] Copy print_img to tmpArr: " + Arrays.toString(BluetoothOrder.print_img));

        this.packageLength += BluetoothOrder.print_img.length;

        int imgSpeed = getImgSpeed();
        if (imgSpeed == 0) {
            imgSpeed = model.getImgPrintSpeed();
        }

        Log.e("eachLinePixToCmdB", "imgSpeed-----" + imgSpeed);

        byte[] feedPaper = feedPaper(imgSpeed);
        System.arraycopy(feedPaper, 0, tmpArr, offset, feedPaper.length);

        Log.i(TAG, "[IMG PRINT TYPE] Copy feedPaper(imgSpeed) to tmpArr: " + Arrays.toString(feedPaper));
    }


    Log.i(TAG, "Some strange shit starting...");
    int i9 = offset + 9;

    this.packageLength += 9;

    int i10 = 0;
    int i11 = 0;
    int i12 = 0;
    while (i10 < length) {
        i11 += 8;
        arrayList.clear();
        arrayList2.clear();
        if (this.newCompress) {
            int i13 = 0;
            b = 0;
            i4 = 0;
            z = false;
            while (true) {
                if (i13 >= width) {
                    i3 = length;
                    break;
                }
                byte b2 = bArr[i12 + i13];
                if (i13 == 0 || b == b2) {
                    i4++;
                } else {
                    dataTrim(i4, b, arrayList2);
                    i4 = 1;
                }
                if (b2 == 0 || z) {
                    i3 = length;
                } else {
                    i3 = length;
                    z = true;
                }
                if (arrayList2.size() > i7) {
                    break;
                }
                if (i13 == width - 1 && i4 != 0 && z) {
                    dataTrim(i4, b2, arrayList2);
                    i4 = 0;
                }
                i13++;
                b = b2;
                length = i3;
            }
        } else {
            i3 = length;
            b = 0;
            i4 = 0;
            z = false;
        }
        i12 += width;
        if (arrayList2.size() > i7 || !this.newCompress) {
            arrayList.clear();
            i12 -= width;
            arrayList.add((byte) -1);
            for (int i14 = 0; i14 < i7; i14++) {
                i12 += 8;
                arrayList.add((byte) (p0[bArr[i12 + 7]] + p1[bArr[i12 + 6]] + p2[bArr[i12 + 5]] + p3[bArr[i12 + 4]] + p4[bArr[i12 + 3]] + p5[bArr[i12 + 2]] + p6[bArr[i12 + 1]] + bArr[i12]));
            }
        } else {
            arrayList.addAll(arrayList2);
        }
        if (!z && arrayList.size() == 0) {
            dataTrim(i4, b, arrayList);
        }
        if (arrayList.size() > 0) {
            byte[] bArr5 = new byte[arrayList.size()];
            for (int i15 = 0; i15 < arrayList.size(); i15++) {
                bArr5[i15] = ((Byte) arrayList.get(i15)).byteValue();
            }
            i11 += arrayList.size();
            String hexString = Integer.toHexString(bArr5.length);
            tmpArr[i9] = 81;
            tmpArr[i9 + 1] = 120;
            if (bArr5[0] == -1 && bArr5.length == i7 + 1) {
                tmpArr[i9 + 2] = -94;
                i11--;
                hexString = Integer.toHexString(i7);
            } else {
                tmpArr[i9 + 2] = -65;
            }
            tmpArr[i9 + 3] = 0;
            tmpArr[i9 + 4] = hexString2Bytes(hexString)[0];
            tmpArr[i9 + 5] = 0;
            if (bArr5[0] == -1 && bArr5.length == i7 + 1) {
                System.arraycopy(bArr5, 1, tmpArr, i9 + 6, bArr5.length - 1);
                c = 0;
            } else {
                c = 0;
                System.arraycopy(bArr5, 0, tmpArr, i9 + 6, bArr5.length);
            }
            if (bArr5[c] == -1 && bArr5.length == i7 + 1) {
                int i16 = i9 + 6;
                tmpArr[i16 + i7] = BluetoothOrder.calcCrc8(tmpArr, i16, i7);
                tmpArr[i9 + 7 + i7] = -1;
                i9 = i9 + 8 + i7;
                this.packageLength = this.packageLength + 8 + i7;
            } else {
                int i17 = i9 + 6;
                tmpArr[bArr5.length + i17] = BluetoothOrder.calcCrc8(tmpArr, i17, bArr5.length);
                tmpArr[i9 + 7 + bArr5.length] = -1;
                i9 = i9 + 8 + bArr5.length;
                this.packageLength = this.packageLength + 8 + bArr5.length;
            }
        }
        i10++;
        length = i3;
    }

    if (getEneragy() != 0) {
        bArr3 = new byte[(i11 + BluetoothOrder.print_text.length + 19)];
    } else {
        bArr3 = new byte[(i11 + BluetoothOrder.print_text.length + 9)];
    }
    System.arraycopy(tmpArr, 0, bArr3, 0, bArr3.length);
    return bArr3;
}


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

def treshold(i_array):
    """
    @param i_array: list(int) where each element represents RGBA pixel encoded as following:
        pixel = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff)
    """
    res = bytearray()
    avg_color = 0
    for pix in i_array:
        avg_color += pix.to_bytes(4, 'big')[3]
    avg_color /= len(i_array)
    avg_color -= 13
    for pix in i_array:
        pix_color = pix.to_bytes(4, 'big')[3]
        if pix_color == 0:
            res.append(0x1) # Black
        elif pix_color > avg_color:
            res.append(0x0) # White
        else:
            res.append(0x1) # Black
    return res


def get_imge_pixels(img):
    pixel = img.load()
    x_lim, y_lim = img.size
    img_arr = []
    for y in range(1, y_lim):
        for x in range(1, x_lim):
            img_arr.append(pixel[x, y])
    return img_arr


if __name__ == '__main__':
    try:
        i_file = sys.argv[1]
        if not os.path.isfile(os.path.abspath(i_file)):
            raise IndexError
    except IndexError:
        print('[E]', f'Usage: {sys.argv[0]} [image]')
        # exit(1)
        i_file = 'greyscale.png'

    #result_img = Dither(path=i_file).floyd_steinberg_dither()

    # pixel = result_img.load()
    # x_lim, y_lim = result_img.size

    # img_arr = []
    # for y in range(1, y_lim):
    #     for x in range(1, x_lim):
    #         img_arr.append(pixel[x, y])

    # print(img_arr[:100])

    new_img = Image.open(i_file)
    new_img = new_img.convert('RGBA')

    # res1 = get_imge_pixels(new_img)
    # print(res1[-1100:-1000])

    pixel = new_img.load()

    x_lim, y_lim = new_img.size

    img_arr = []
    for y in range(1, y_lim):
        for x in range(1, x_lim):
            img_arr.append(pixel[x, y])

    colors_arr = []
    for pix in img_arr:
        R, G, B, A = pix
        color = (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff)
        colors_arr.append(color)

    print(treshold(colors_arr)[:10])
