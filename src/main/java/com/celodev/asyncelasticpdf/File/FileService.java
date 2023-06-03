package com.celodev.asyncelasticpdf.File;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileService {
  private final Path root = Paths.get("uploads");


  public Mono<File> saveFile(FilePart filePart) {
    return Mono.defer(() -> {
      File file = toFile(filePart);
      return filePart.transferTo(file).thenReturn(file);
    });
  }

  public File toFile(FilePart filePart) {
    return new File(Paths.get("uploads").resolve(filePart.filename()).toString());
  }

  public Flux<DataBuffer> load(String filename) {
    try {
      Path file = root.resolve(filename);
      Resource resource = new UrlResource(file.toUri());

      if (resource.exists() || resource.isReadable()) {
        return DataBufferUtils.read(resource, new DefaultDataBufferFactory(), 4096);
      } else {
        throw new RuntimeException("Could not read the file!");
      }
    } catch (MalformedURLException e) {
      throw new RuntimeException("Error: " + e.getMessage());
    }
  }

  public Stream<File> loadAll() throws IOException {
    return Files.walk(Paths.get("/home/marcelo/marcelo/code/backend/spring/api-diario/edicoes"))
      .filter(Files::isRegularFile)
      .map(Path::toFile)
      .filter(file -> !file.isDirectory());
  }

  public Mono<InputStream> toInputStream(FilePart filePart) {
    return DataBufferUtils.join(filePart.content())
      .map(dataBuffer -> new byte[dataBuffer.readableByteCount()])
      .map(ByteArrayInputStream::new);
  }
}