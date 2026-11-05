import importlib.util
from pathlib import Path
import struct
import unittest

spec = importlib.util.spec_from_file_location("native", Path(__file__).resolve().parents[2] / "scripts/check-native-pages.py")
native = importlib.util.module_from_spec(spec)
spec.loader.exec_module(native)


def elf(alignment=16384, relro_size=16384, writable_size=32768):
    data = bytearray(64 + 2 * 56)
    data[:6] = b"\x7fELF\x02\x01"
    struct.pack_into("<Q", data, 32, 64)
    struct.pack_into("<HH", data, 54, 56, 2)
    struct.pack_into("<IIQQQQQQ", data, 64, 1, 6, 0, 0, 0, 0, writable_size, alignment)
    struct.pack_into("<IIQQQQQQ", data, 120, 0x6474E552, 4, 0, 0, 0, 0, relro_size, 1)
    return data


class NativePagesTests(unittest.TestCase):
    def test_aligned_load_and_relro_pass(self):
        self.assertEqual("PASS", native.inspect_elf(elf())["state"])

    def test_small_load_alignment_fails(self):
        self.assertEqual("FAIL", native.inspect_elf(elf(alignment=4096))["state"])

    def test_unaligned_relro_followed_by_writable_data_fails(self):
        self.assertEqual("FAIL", native.inspect_elf(elf(relro_size=4096))["state"])

    def test_unaligned_relro_occupying_entire_load_passes(self):
        self.assertEqual("PASS", native.inspect_elf(elf(relro_size=4096, writable_size=4096))["state"])

    def test_invalid_and_truncated_input_is_rejected(self):
        for data in (b"", b"not an ELF", elf()[:50]):
            with self.subTest(size=len(data)), self.assertRaises((ValueError, struct.error)):
                native.inspect_elf(data)
