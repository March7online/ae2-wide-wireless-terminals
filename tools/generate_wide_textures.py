#!/usr/bin/env python3

import argparse
from pathlib import Path
from zipfile import ZipFile

from PIL import Image


EXTRA_COLUMNS = 9
SLOT_PITCH = 18
EXTRA_WIDTH = EXTRA_COLUMNS * SLOT_PITCH
HALF_EXTRA_WIDTH = EXTRA_WIDTH // 2

GRID_LEFT = 7
GRID_WIDTH = 9 * SLOT_PITCH
NETWORK_BOTTOM = 71

BODY_WIDTH = 176
BODY_CONTENT_LEFT = 7
BODY_CONTENT_RIGHT = 170


def read_png(jar_path: Path, resource_path: str) -> Image.Image:
    with ZipFile(jar_path) as archive:
        with archive.open(resource_path) as source:
            return Image.open(source).convert("RGBA")


def fill_from_column(
        target: Image.Image,
        source: Image.Image,
        source_x: int,
        destination_x: int,
        width: int,
        top: int,
        bottom: int) -> None:
    column = source.crop((source_x, top, source_x + 1, bottom))
    target.paste(column.resize((width, bottom - top), Image.Resampling.NEAREST),
                 (destination_x, top))


def expand_body(
        target: Image.Image,
        source: Image.Image,
        source_screen_width: int,
        top: int,
        bottom: int) -> None:
    shifted_content_left = BODY_CONTENT_LEFT + HALF_EXTRA_WIDTH
    shifted_content_right = BODY_CONTENT_RIGHT + HALF_EXTRA_WIDTH
    shifted_body_right = BODY_CONTENT_RIGHT + EXTRA_WIDTH
    expanded_body_width = BODY_WIDTH + EXTRA_WIDTH

    target.paste(source.crop((0, top, BODY_CONTENT_LEFT, bottom)), (0, top))
    fill_from_column(target, source, BODY_CONTENT_RIGHT, BODY_CONTENT_LEFT,
                     HALF_EXTRA_WIDTH, top, bottom)
    target.paste(
        source.crop((BODY_CONTENT_LEFT, top, BODY_CONTENT_RIGHT, bottom)),
        (shifted_content_left, top))
    fill_from_column(target, source, BODY_CONTENT_RIGHT, shifted_content_right,
                     HALF_EXTRA_WIDTH, top, bottom)
    target.paste(source.crop((BODY_CONTENT_RIGHT, top, BODY_WIDTH, bottom)),
                 (shifted_body_right, top))
    target.paste(source.crop((BODY_WIDTH, top, source_screen_width, bottom)),
                 (expanded_body_width, top))


def expand_terminal_texture(
        source: Image.Image,
        source_screen_width: int,
        bottom_end: int) -> Image.Image:
    target_width = source_screen_width + EXTRA_WIDTH
    target = Image.new("RGBA", (target_width, source.height))

    target.paste(source.crop((0, 0, GRID_LEFT, NETWORK_BOTTOM)), (0, 0))
    grid = source.crop((GRID_LEFT, 0, GRID_LEFT + GRID_WIDTH, NETWORK_BOTTOM))
    target.paste(grid, (GRID_LEFT, 0))
    target.paste(grid, (GRID_LEFT + GRID_WIDTH, 0))
    target.paste(
        source.crop((GRID_LEFT + GRID_WIDTH, 0, source_screen_width, NETWORK_BOTTOM)),
        (GRID_LEFT + GRID_WIDTH * 2, 0))

    expand_body(target, source, source_screen_width, NETWORK_BOTTOM, bottom_end)
    return target


def expand_pattern_texture(
        source: Image.Image,
        source_screen_width: int,
        bottom_end: int) -> Image.Image:
    target = expand_terminal_texture(source, source_screen_width, bottom_end)
    target.paste((0, 0, 0, 0), (0, NETWORK_BOTTOM, target.width, bottom_end))
    target.paste(
        source.crop((0, NETWORK_BOTTOM, source_screen_width, bottom_end)),
        (HALF_EXTRA_WIDTH, NETWORK_BOTTOM))
    return target


def expand_wtlib_extras(source: Image.Image) -> Image.Image:
    source_screen_width = 200
    bottom_end = 177
    target = Image.new("RGBA", (source_screen_width + EXTRA_WIDTH, source.height))

    upper_end = 70
    target.paste(source.crop((0, 0, source_screen_width, upper_end)),
                 (HALF_EXTRA_WIDTH, 0))
    # WTLib's lower crafting panel is a fixed 200px composition. Keep it
    # centered at the same offset as the upper panel instead of stretching
    # its edge pixels across the added nine columns.
    target.paste(source.crop((0, upper_end, source_screen_width, bottom_end)),
                 (HALF_EXTRA_WIDTH, upper_end))
    return target


def parse_args() -> argparse.Namespace:
    parser = argparse.ArgumentParser(
        description="Generate 18-column AE2 wireless terminal textures.")
    parser.add_argument("--ae2-jar", required=True, type=Path)
    parser.add_argument("--wtlib-jar", required=True, type=Path)
    parser.add_argument("--output", required=True, type=Path)
    return parser.parse_args()


def main() -> None:
    args = parse_args()
    args.output.mkdir(parents=True, exist_ok=True)

    ae2_textures = {
        "terminal_wide.png": ("assets/ae2/textures/guis/terminal.png", 195, 168),
        "crafting_wide.png": ("assets/ae2/textures/guis/crafting.png", 195, 241),
    }
    for output_name, (resource_path, screen_width, bottom_end) in ae2_textures.items():
        source = read_png(args.ae2_jar, resource_path)
        expanded = expand_terminal_texture(source, screen_width, bottom_end)
        expanded.save(args.output / output_name, optimize=True)

    pattern = read_png(args.ae2_jar, "assets/ae2/textures/guis/pattern.png")
    expand_pattern_texture(pattern, 195, 249).save(
        args.output / "pattern_wide.png", optimize=True)

    extras = read_png(args.wtlib_jar, "assets/ae2/textures/wtlib/guis/extras.png")
    expand_wtlib_extras(extras).save(args.output / "wtlib_extras_wide.png", optimize=True)


if __name__ == "__main__":
    main()
